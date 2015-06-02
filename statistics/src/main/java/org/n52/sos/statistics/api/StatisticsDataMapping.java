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

public class StatisticsDataMapping {

    // ---------------- DEFAULT VALUES --------------//
    public static final String TIMESTAMP_FIELD = "@timestamp";

    public static final String VERSION_FIELD = "version";

    public static final String SERVICE_FIELD = "service";

    public static final String LANGUAGE_FIELD = "language";

    public static final String OPERATION_NAME_FIELD = "operation_name";

    public static final String IP_ADDRESS_FIELD = "source-ip-address";

    public static final String SOURCE_GEOLOC_FIELD = "source-geolocation";

    public static final String PROXIED_REQUEST_FIELD = "proxied-request";

    // --------------- GETCAPABILITIES --------------//
    /**
     * Arrays of string
     */
    public static final String GC_VERSIONS_FIELD = "getcapabilities-versions";

    /**
     * Arrays of string
     */
    public static final String GC_FORMATS_FIELD = "getcapabilities-formats";

    // --------------- DESCRIBE SENSOR --------------//
    public static final String DS_PROCEDURE = "describesensor-procedure";

    public static final String DS_PROCEDURE_DESC_FORMAT = "describesensor-procedure-description-format";

    public static final String DS_VALID_TIME = "describesensor-validtime";

    // --------------- GET OBSERVATION --------------//
    public static final String GO_PROCEDURES = "getobservation-procedures";

    public static final String GO_SPATIAL_FILTER = "getobservation-spatial-filter";

    public static final String GO_OBSERVED_PROPERTIES = "getobservation-observed-properties";

    public static final String GO_OFFERINGS = "getobservation-offerings";
}
