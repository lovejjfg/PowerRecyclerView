/*
 * Copyright (c) 2016.  Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovejjfg.powerrecyclerx;

import androidx.annotation.IntDef;
import com.lovejjfg.powerrecyclerx.model.ISelect;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by Joe on 2016/12/1.
 * Email lovejjfg@gmail.com
 */

@Target(PARAMETER)
@IntDef(flag = true, value = {
        ISelect.SINGLE_MODE,
        ISelect.MULTIPLE_MODE
})
@Retention(RetentionPolicy.SOURCE)
public @interface SelectMode {

}

