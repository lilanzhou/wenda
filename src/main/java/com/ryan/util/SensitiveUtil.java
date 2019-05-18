package com.ryan.util;
import org.apache.commons.lang.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019:05:01
 *
 * @Author : Lilanzhou
 * 功能 :
 */
@Component
public class SensitiveUtil implements InitializingBean {

    private static Logger logger=LoggerFactory.getLogger(SensitiveUtil.class);

    //默认敏感词替代符
    private static final String DEFAULT_REPLACEMENT="***";
    private class TrieNode{
         // 敏感词的结尾
        private boolean end=false;
        /**
         *key下一个字符，value对应的节点
         */
        private Map<Character,TrieNode> subNodes=new HashMap<>();

        void addSubNode(Character key,TrieNode node){
            subNodes.put(key,node);
        }
        //获取下一个节点
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeywordEnd(){
            return end;
        }

        void setKeywordEnd(boolean end){
            this.end=end;
        }
        public int getSubNodeCount() {
            return subNodes.size();
        }
    }

    /**
     * root 根节点
     */
    private TrieNode root=new TrieNode();


    public boolean isSymbol(char c){
        int ic=(int)c;
        return !CharUtils.isAsciiAlphanumeric(c)&&(ic < 0x2E80 || ic > 0x9FFF);
    }

    public String filter(String text){
        if(" ".equals(text)){
            return text;
        }
        String replacement=DEFAULT_REPLACEMENT;
        StringBuilder result=new StringBuilder();

        //三个指针
        TrieNode tempNode=root;
        int begin=0; //来回对比
        int position=0;//当前比较的位置

        while(position<text.length()){
            char c=text.charAt(position);

            if(isSymbol(c)){
                if(tempNode==root){
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            //下一个节点
            tempNode=tempNode.getSubNode(c);
            //如果当前的位置结束
            if(tempNode==null){
                result.append(text.charAt(begin));
                //位置向后挪一位
                position=begin+1;
                //检测指针回到position位置
                begin=position;
                //指针回到初始位置
                tempNode=root;
            }else if(tempNode.isKeywordEnd()){
                 //发现敏感词
                result.append(replacement);
                position=position+1;//直接
                begin=position;
                tempNode=root;
            }else {
                ++position;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }
    private void addWord(String lineTxt) {
        TrieNode tempNode = root;
        // 循环每个字节
        for (int i = 0; i < lineTxt.length(); ++i) {
            Character c = lineTxt.charAt(i);
            // 过滤空格
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);

            if (node == null) { // 没初始化
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }

            tempNode = node;

            if (i == lineTxt.length() - 1) {
                // 关键词结束， 设置结束标志
                tempNode.setKeywordEnd(true);
            }
        }
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        root = new TrieNode();

        try {
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

}
