package com.healthyfish.healthyfish.POJO;

/**
 * 描述：
 * 作者：LYQ on 2017/7/11.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanPhyQuestionnaireTest {

    private String content; // 问题
    private String answer; // 答案
    public  static String[] answers = { "没有", "很少", "有时", "经常","总是" }; // 选项编号和答案编号

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }
}
