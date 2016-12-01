package com.lovejjfg.powerrecycle.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.lovejjfg.powerrecycle.AdapterLoader.STATE_ERROR;
import static com.lovejjfg.powerrecycle.AdapterLoader.STATE_LASTED;
import static com.lovejjfg.powerrecycle.AdapterLoader.STATE_LOADING;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by Joe on 2016/12/1.
 * Email lovejjfg@gmail.com
 */
@IntDef(flag = true, value = {
        STATE_LOADING,
        STATE_LASTED,
        STATE_ERROR
})
@Target(PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface LoadState {
}
