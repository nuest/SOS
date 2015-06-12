# OGC Web Service Statistics Module for 52°North web services

## About

This module allows to capture usage statistics for OGC web services into a document database for interactive visualisation and long-term statistics of service and domain specific meta-information.

Project description: https://wiki.52north.org/bin/view/Projects/GSoC2015Statistics4Ows

## Architectural overview


The main entry point for collection of service statistics is the class ``org.n52.sos.statistics.sos.SosStatisticsLoggerListener``. It receives the events from the ``SosEventBus``.

In the event listener, the events are distributed to different **resolver** classes (which implement ``org.n52.sos.statistics.api.interfaces.IStatisticsEventResolver``) based on the event's class. The resolver is provided with the required objects/information to distribute the processing of the event.

For example, the ``SosRequestEventResolver``, is provided with the instances of classes extending ``AbstractServiceRequest``. Within the resolver, the different implementing classes are further distributed to **handlers**.

In the handlers, the data is extracted from the provided objects and a `Map<String,Object>` is returned.

This `Map<String,Object>` is persisted to the statistics database, which is based on Elasticsearch.

The Elasticsearch client API automatically converts the map to a JSON string, including nested map objects, e.g. ``Map<String,Map<String,Object>>``.

## Setup

- Checkout this branch.
- Install Elasticsearch on your local machine in version 1.6.x or later from https://www.elastic.co/downloads/elasticsearch
- Open up the Elasticsearch config file under the ``config/elasticsearch.yml``. Modify the clustername to ``ogc-statistics-clluster``, i.e. ``cluster.name: ogc-statistics-cluster``
- Install Kibana 4.x on your local machine from https://www.elastic.co/downloads/kibana
- Run ``mvn clean install -DskipTests=true`` from the SOS root project.
- Run ``mvn jetty:run`` from the webapp module. If the port is already occupied, open the file ``webapp/pom.xml`` and change it under the Jetty `<plugin>` section.
- Goto http://localhost:8080 and set up your SOS deployment, i.e. configure a database. For testing, use "H2/GeoDB (in memory)". 
- Start Elasticsearch with the appropriate executable in ``<Elasticsearch directory>/bin``, e.g. ``bin/elasticsearch.bat`` on Windows 
- Start Kibana with the appropriate executable in ``<Kibana directory>/bin``, e.g. ``bin\kibana.bat`` on Windows, and open it at http://localhost:5601/
- Import the [SoupUI](http://www.soapui.org/) project to your SoapUI workspace (File -> Import Project): ``<statics module>/src/test/resources/soapUI/SOSBulkRequests.xml``
- Run the test cases from the SoupUI.
- Set up the index in Kibana, the default index name is ``ogc-statistics-clluster``and the timestamp field is ``@timestamp``

![Kibana set up index](https://wiki.52north.org/pub/Projects/GSoC2015Statistics4Ows/kibana-index-setup.JPG)

- Head to the [discover mode](https://www.elastic.co/guide/en/kibana/current/discover.html) and check if you see any raw data there.

![Discover mode](https://wiki.52north.org/pub/Projects/GSoC2015Statistics4Ows/discover-mode.JPG)

- With the default Kibana settings setup, click on the link below to view the bar chart of the number of counts by request types this year.

![Bar chart](https://wiki.52north.org/pub/Projects/GSoC2015Statistics4Ows/kibana-countoperations.JPG)

http://localhost:5601/#/visualize/create?type=histogram&indexPattern=myindex&_g=(refreshInterval:(display:Off,section:0,value:0),time:(from:now%2Fy,mode:quick,to:now%2Fy))&_a=(filters:!(),linked:!f,query:(query_string:(analyze_wildcard:!t,query:'*')),vis:(aggs:!((id:'1',params:(),schema:metric,type:count),(id:'2',params:(field:operation-name,order:desc,orderBy:'1',size:5),schema:segment,type:terms)),listeners:(),params:(addLegend:!t,addTooltip:!t,defaultYExtents:!f,mode:stacked,shareYAxis:!t),type:histogram))
