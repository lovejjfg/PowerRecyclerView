package com.lovejjfg.powerrecycle.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.lovejjfg.powerrecycle.AdapterLoader.MultipleMode;
import static com.lovejjfg.powerrecycle.AdapterLoader.SingleMode;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by Joe on 2016/12/1.
 * Email lovejjfg@gmail.com
 */

@Target(PARAMETER)
@IntDef(flag = true, value = {
        SingleMode,
        MultipleMode
})
@Retention(RetentionPolicy.SOURCE)
public @interface ChoiceMode {
}

