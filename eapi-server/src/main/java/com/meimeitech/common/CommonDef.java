package com.meimeitech.common;
/**
 * <B>系统名称：</B>美美金融平台<BR>
 * <B>模块名称：</B>基金代销模块<BR>
 * <B>中文类名：</B>基金产品服务接口<BR>
 * <B>概要说明：</B><BR>
 *
 * @author 北京鹏润美美科技有限公司（yaojian）
 * @since 2017/7/1
 */
public class CommonDef {

    /**
     * 用户风险评分答案A=4分
     */
    public static final int SCORE_A = 4;

    /**
     * 用户风险评分答案B=3分
     */
    public static final int SCORE_B = 3;

    /**
     * 用户风险评分答案C=2分
     */
    public static final int SCORE_C = 2;

    /**
     * 用户风险评分答案D=1分
     */
    public static final int SCORE_D = 1;

    /**
     * 一个月默认天数
     */
    public static final int MONTH = 30;

    /**
     * 申购费率
     */
    public static final double SUBSCRIBE_RATE = 0;

    /**
     * 产品大类1001 基金 1002 保险 1003短期固收（利得宝
     */
    public static final String PRODUCT_CATEGORY = "1001";

    /**
     * 基金类型枚举
     */
    public enum FUND_PRODUCT_TYPE{
        GP("01","股票型"),
        Z2("02","债券型"),
        HH("03","混合型"),
        HB("04","货币型"),
        ZS("05","指数型"),
        BB("06","保本型"),
        ETF("07","ETF联接"),
        QDII("08","QDII"),
        LOF("09","LOF"),
        DQLC("98","短期理财型"),
        JJZH("20","基金组合");
        private String value;
        private String name;
        FUND_PRODUCT_TYPE(String value,String name){
            this.value=value;
            this.name=name;
        }
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public static FUND_PRODUCT_TYPE getByValue(String value){
            for (FUND_PRODUCT_TYPE belongSystem : FUND_PRODUCT_TYPE.values()) {
                if(value.equals(belongSystem.getValue())){
                    return belongSystem;
                }
            }
            return null;
        }
    }

    /**
     * 基金风险等级枚举
     */
    public enum FUND_PRODUCT_RISK_LEVEL{
        BS(1,"保守型","低"),
        WJ(2,"稳健型","中低"),
        CS(3,"成熟型","中"),
        JJ(4,"进取型","中高"),
        GG(5,"激进型","高");
        private Integer value;
        private String name;
        private String level;
        FUND_PRODUCT_RISK_LEVEL(Integer value,String name,String level){
            this.value=value;
            this.name=name;
            this.level=level;
        }
        public Integer getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public String getLevel() {
            return level;
        }
        public static FUND_PRODUCT_RISK_LEVEL getByValue(int value){
            for (FUND_PRODUCT_RISK_LEVEL belongSystem : FUND_PRODUCT_RISK_LEVEL.values()) {
                if(value == belongSystem.getValue()){
                    return belongSystem;
                }
            }
            return null;
        }
    }

    /**
     * 基金公告类型枚举
     */
    public enum FUND_PRODUCT_NOTICE_TYPE{
        ZMSMS(1,"招募说明书"),
        JJHT(2,"基金合同"),
        DQBG(3,"定期报告"),
        PUGG(4,"普通公告"),
        SGSH(5,"申购赎回"),
        FXYZ(6,"发行运作"),
        SYFP(7,"收益分配"),
        GZTZ(8,"估值调整"),
        ETF(9,"ETF发行运作");
        private Integer value;
        private String name;
        FUND_PRODUCT_NOTICE_TYPE(Integer value,String name){
            this.value=value;
            this.name=name;
        }
        public Integer getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public static FUND_PRODUCT_NOTICE_TYPE getByValue(int value){
            for (FUND_PRODUCT_NOTICE_TYPE belongSystem : FUND_PRODUCT_NOTICE_TYPE.values()) {
                if(value == belongSystem.getValue()){
                    return belongSystem;
                }
            }
            return null;
        }
    }

    /**
     * 基金排名类型枚举
     */
    public enum FUND_PRODUCT_RANK_TYPE{
        JNYL(1,"今年以来"),
        ZJYZ(2,"最近一周"),
        ZJYY(3,"最近一月"),
        ZJSY(4,"最近三月"),
        ZJLY(5,"最近六月"),
        ZJYN(6,"最近一年"),
        ZJLN(7,"最近两年"),
        ZJSN(8,"最近三年"),
        ZJSIN(9,"最近四年"),
        ZJWN(10,"最近五年"),
        ZJLIUN(11,"最近六年"),
        CLYL(12,"成立以来");
        private Integer value;
        private String name;
        FUND_PRODUCT_RANK_TYPE(Integer value,String name){
            this.value=value;
            this.name=name;
        }
        public Integer getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public static FUND_PRODUCT_RANK_TYPE getByValue(int value){
            for (FUND_PRODUCT_RANK_TYPE belongSystem : FUND_PRODUCT_RANK_TYPE.values()) {
                if(value == belongSystem.getValue()){
                    return belongSystem;
                }
            }
            return null;
        }
    }


    /**
     * 基金账户认证状态
     */
    public enum FUND_ACCOUNT_AUTHSTATUS{
        // 0:待认证 1 已认证 3 认证中 4 认证失败
        WAITAUTH("0","待认证"),
        AUTH("1","已认证"),
        STAYAUTH("3","认证中"),
        AUTHFAIL("4","认证失败");
        private String value;
        private String name;
        FUND_ACCOUNT_AUTHSTATUS(String value,String name){
            this.value=value;
            this.name=name;
        }
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public static FUND_ACCOUNT_AUTHSTATUS getByValue(String value){
            for (FUND_ACCOUNT_AUTHSTATUS belongSystem : FUND_ACCOUNT_AUTHSTATUS.values()) {
                if(value == belongSystem.getValue()){
                    return belongSystem;
                }
            }
            return null;
        }
    }

    /**
     * 利得开户状态
     */
    public enum LEAD_OPEN_ACCOUNT_STATUS{
        // S-成功F-失败P-进行中
        SUCCESS("S","成功"),
        FAIL("F","失败"),
        UNDERWAY("P","进行中");
        private String value;
        private String name;
        LEAD_OPEN_ACCOUNT_STATUS(String value,String name){
            this.value=value;
            this.name=name;
        }
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public static LEAD_OPEN_ACCOUNT_STATUS getByValue(String value){
            for (LEAD_OPEN_ACCOUNT_STATUS belongSystem : LEAD_OPEN_ACCOUNT_STATUS.values()) {
                if(value == belongSystem.getValue()){
                    return belongSystem;
                }
            }
            return null;
        }
    }

    /**
     * 利得开户客户类型
     * 0-机构，1-个人（默认）
     */
    public enum LEAD_CUSTTYPE{
        ORGANIZATION("0","机构"),
        PERSON("1","个人");
        private String value;
        private String name;
        LEAD_CUSTTYPE(String value,String name){
            this.value=value;
            this.name=name;
        }
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public static LEAD_CUSTTYPE getByValue(String value){
            for (LEAD_CUSTTYPE belongSystem : LEAD_CUSTTYPE.values()) {
                if(value == belongSystem.getValue()){
                    return belongSystem;
                }
            }
            return null;
        }
    }

    /**
     * 基金产品是否认购期
     *  0-否 1-是(组合为空)
     */
    public enum FUND_PRODUCT_SUBS_STATUS{
        STATUS_0("0","否"),STATUS_1("1","是");
        private String value;
        private String name;
        FUND_PRODUCT_SUBS_STATUS(String value,String name){
            this.value=value;
            this.name=name;
        }
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public static FUND_PRODUCT_SUBS_STATUS getByValue(String value){
            for (FUND_PRODUCT_SUBS_STATUS belongSystem : FUND_PRODUCT_SUBS_STATUS.values()) {
                if(value.equals(belongSystem.getValue())){
                    return belongSystem;
                }
            }
            return null;
        }
    }

    /**
     * 分红方式(默认现金分红)0红利再投1 现金分红(组合传入0)
     */
    public enum FUND_PRODUCT_DIVIDENT_METHOD{
        METHOD_0("0","红利再投"),METHOD_1("1","现金分红");
        private String value;
        private String name;
        FUND_PRODUCT_DIVIDENT_METHOD(String value,String name){
            this.value=value;
            this.name=name;
        }
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public static FUND_PRODUCT_DIVIDENT_METHOD getByValue(String value){
            for (FUND_PRODUCT_DIVIDENT_METHOD belongSystem : FUND_PRODUCT_DIVIDENT_METHOD.values()) {
                if(value.equals(belongSystem.getValue())){
                    return belongSystem;
                }
            }
            return null;
        }
    }

    /**
     * 订单确认状态
     0-确认失败
     1-确认成功
     2-部分确认
     3-已撤销交易
     5-认购行为确认
     9-确认中（下单成功后的默认状态）
     W-待受理
     */
    public enum ORDER_CONFIRM_STATUS{
        STATUS_W("W","待受理"),
        STATUS_0("0","确认失败"),
        STATUS_1("1","确认成功"),
        STATUS_2("2","部分确认"),
        STATUS_3("3","已撤销交易"),
        STATUS_5("5","认购行为确认"),
        STATUS_9("9","确认中");
        private String value;
        private String name;
        ORDER_CONFIRM_STATUS(String value,String name){
            this.value=value;
            this.name=name;
        }
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public static ORDER_CONFIRM_STATUS getByValue(String value){
            for (ORDER_CONFIRM_STATUS belongSystem : ORDER_CONFIRM_STATUS.values()) {
                if(value.equals(belongSystem.getValue())){
                    return belongSystem;
                }
            }
            return null;
        }
    }

    /**
     * 支付扣款标记 0-未扣款1-已扣款 不传时，默认为0-未扣款
     */
    public enum ORDER_PAY_FLAG{
        PAY_FLAG_0("0","未扣款"),
        PAY_FLAG_1("1","已扣款");
        private String value;
        private String name;
        ORDER_PAY_FLAG(String value,String name){
            this.value=value;
            this.name=name;
        }
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public static ORDER_PAY_FLAG getByValue(String value){
            for (ORDER_PAY_FLAG belongSystem : ORDER_PAY_FLAG.values()) {
                if(value.equals(belongSystem.getValue())){
                    return belongSystem;
                }
            }
            return null;
        }
    }

    /**
     * 订单交易状态
     0-下单失败（若认申购业务，说明支付失败或异常）
     1-下单成功（若认申购业务，说明支付成功）
     2-已撤销交易
     9-订单支付中
     W-待受理
     */
    public enum ORDER_TRANS_STATUS{
        STATUS_W("W","待受理"),
        STATUS_0("0","下单失败"),
        STATUS_1("1","下单成功"),
        STATUS_2("2","已撤销交易"),
        STATUS_9("9","订单支付中");
        private String value;
        private String name;
        ORDER_TRANS_STATUS(String value,String name){
            this.value=value;
            this.name=name;
        }
        public String getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
        public static ORDER_TRANS_STATUS getByValue(String value){
            for (ORDER_TRANS_STATUS belongSystem : ORDER_TRANS_STATUS.values()) {
                if(value.equals(belongSystem.getValue())){
                    return belongSystem;
                }
            }
            return null;
        }
    }
}
