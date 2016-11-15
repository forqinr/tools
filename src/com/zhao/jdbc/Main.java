package com.zhao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 * 去除重复的印花税缴纳数据
 * <p/>
 * 使用到DBUtils工具类
 *
 * @author zhaoyan
 * @since 2016.11.15 10:21
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        // 注册DB连接
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        String url = "jdbc:db2://127.0.0.1/test";
        String user = "test";
        String password = "test";
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // 使用BeanListHandler
        ResultSetHandler<List<TaxBean>> handler = new BeanListHandler<TaxBean>(TaxBean.class);

        String selectSql = "SELECT C.* FROM tb_contract_tax C WHERE C.CONTRACT_ID IN (SELECT a.CONTRACT_ID FROM (SELECT CONTRACT_ID, COUNT(contract_id) FROM tb_contract_tax GROUP BY CONTRACT_ID HAVING COUNT(CONTRACT_ID)>1) a) order by contract_id";

        String updateSql = "delete from tb_contract_tax where id = ?";

        QueryRunner runner = new QueryRunner();

        List<TaxBean> taxbeanList = null;
        try {
            // 在此给BeanListHandler对象赋值
            taxbeanList = runner.query(con, selectSql, handler);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                System.exit(0);
            }
        }

        ListIterator<TaxBean> iterator = taxbeanList.listIterator();

        TaxBean tempBean = iterator.next();

        while (iterator.hasNext()) {
            TaxBean bean = iterator.next();
            if (!bean.getCONTRACT_ID().equals(tempBean.getCONTRACT_ID())) {
                tempBean = bean;
                continue;
            } else {
                if (bean.getCONTRACT_ID().equals(tempBean.getCONTRACT_ID()) && bean.getTAX_AMOUNT().equals(tempBean
                        .getTAX_AMOUNT()) && bean.getTAX_PERSON().equals(tempBean.getTAX_PERSON())) {
                    try {
                        runner.update(con, updateSql, bean.getID());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        try {
                            con.rollback();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }

        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
