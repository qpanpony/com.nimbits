/*
 * Copyright (c) 2013 Nimbits Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package com.nimbits.server.cache;

import com.nimbits.server.transaction.cache.NimbitsCache;

import javax.cache.CacheException;

import java.util.logging.Logger;


public class CacheFactory {
    final static Logger logger = Logger.getLogger(CacheFactory.class.getName());

    public static NimbitsCache getInstance() {

        try {
            return new AppEngineCache();
        } catch (CacheException e) {
            logger.severe("Cache Engine caught exception");
            e.printStackTrace();
            return null;
        }
    }


}
