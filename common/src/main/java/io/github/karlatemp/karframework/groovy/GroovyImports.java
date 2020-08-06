/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/07 24:27:01
 *
 * kar-framework/kar-framework.common.main/GroovyImports.java
 */

package io.github.karlatemp.karframework.groovy;

import java.util.Map;

public class GroovyImports extends GroovyRequire {
    public GroovyImports(GroovyScript script) {
        super(script);
    }

    @Override
    protected Object post(GroovyScript script) {
        @SuppressWarnings("unchecked")
        Map<String, Object> all = (Map<String, Object>) super.post(script);
        if (all != null) {
            this.script.variables.putAll(all);
        }
        return all;
    }
}
