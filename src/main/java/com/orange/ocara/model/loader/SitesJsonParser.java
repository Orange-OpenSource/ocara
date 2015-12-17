/**
 * Copyright (C) 2015 Orange
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.orange.ocara.model.loader;

import com.orange.ocara.model.Site;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

public class SitesJsonParser extends AbstractJsonParser {

    @Getter
    private List<Site> sites = null;


    @SuppressWarnings("unchecked")
    protected void doLoad(InputStream jsonStream) throws IOException {

        sites = new ArrayList<Site>();

        HashMap[] jsonSites = readValue(jsonStream, HashMap[].class);

        for(HashMap jsonSite : jsonSites) {
            Site site = new Site();
            site.setName( extractString(jsonSite, "name") );
            site.setLabelUgi( extractString(jsonSite, "labelUGI") );
            site.setUgi( extractString(jsonSite, "idUGI") );
            site.setNoImmo( extractString(jsonSite, "NOIMMO") );
            Map jsonAddress = extractMap(jsonSite, "addr");
            site.setStreet(  extractString(jsonAddress, "street") );
            site.setCity( extractString(jsonAddress, "city") );
            site.setCode( extractInt(jsonAddress, "cp") );


            sites.add(site);
        }
    }


}


