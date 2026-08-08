package cn.xx.test.types.rule02.logic;

import org.springframework.stereotype.Service;

/**
 * @author xiaoxin
 * @description
 * @create 2026/8/8 16:41
 */
public class XxxResponse {
        private final String age;

        public XxxResponse(String age) {
            this.age = age;
        }

        public String getAge() {
            return age;
        }
}

