/**
 * Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.statistics.api;

import org.elasticsearch.common.settings.Settings;

public class ElasticSearchSettings {

    /**
     * Is statistics collection enable
     */
    private final boolean loggingEnabled;

    /**
     * In LAN mode the clustername to join to.
     */
    private String clusterName;

    /**
     * In not LAN mode the remote cluster to join to.
     */
    private String address;

    /**
     * Is the connection type Remote or LAN
     */
    private boolean inLan = true;

    /**
     * The ElasticSearch indexId of the date to be persisted under
     */
    private String indexId;

    /**
     * TypeId of the date to be persisted under
     */
    private String typeId = "ogctype";

    /**
     * ElasticSearch specific settings. Will be added at the connection
     * creation.
     */
    private Settings advancedSettings;

    public ElasticSearchSettings(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    // Getter Setters

    public String getIndexId()
    {
        return indexId;
    }

    public void setIndexId(String indexId)
    {
        this.indexId = indexId;
    }

    public String getTypeId()
    {
        return typeId;
    }

    public void setTypeId(String typeId)
    {
        this.typeId = typeId;
    }

    public String getClusterName()
    {
        return clusterName;
    }

    public boolean isLoggingEnabled()
    {
        return loggingEnabled;
    }

    public void setClusterName(String clusterName)
    {
        this.clusterName = clusterName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public boolean isInLan()
    {
        return inLan;
    }

    public void setInLan(boolean inLan)
    {
        this.inLan = inLan;
    }

    public Settings getAdvancedSettings()
    {
        return advancedSettings;
    }

    public void setAdvancedSettings(Settings advancedSettings)
    {
        this.advancedSettings = advancedSettings;
    }

    @Override
    public String toString()
    {
        return "ElasticSearchSettings [loggingEnabled=" + loggingEnabled + ", clusterName=" + clusterName + ", address=" + address + ", inLan=" + inLan + ", indexId=" + indexId + ", typeId=" + typeId
                + ", advancedSettings=" + advancedSettings + "]";
    }

}
