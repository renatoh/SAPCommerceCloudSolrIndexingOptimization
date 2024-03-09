Work in Progress!!

# Problem Statement
Many SAP Commerce Cloud (called SAP CC) deployments are building the Solr product index from scratch every 24h in order to have reflect the latest prices, stock levels, etc in the product search. These job often run for hours and are rebuilding  indexes with 100k+ product documents in it.
During the time of indexing most if not all the data pushed to the index is fetched from the database SAP CC is running on. This puts a lot of pressure onto the DB and very often the queries to the database fetching all the product attribute becomes the bottleneck of the Solr-indexing Job.


Playing around with some settings e.g. increasing the batch-size and thread pool-size for the worker thread is sometimes shorting the indexing time but it cannot overcome one fundamental flaw within the SAP CC’s Solr-integration and this issue is the following:
All the business logic required to fetch and calculate the attributes for the Solr-documents has to be put within ValueResolver (successor of FieldValueProvider). Every attribute requiring more business logic than what fits reasonably within a Spring-Expression, requires its very own ValueResolver, therefore dozens of them will be created.
These  ValueResolver are executed independently of each other, there is no overarching context or the option to share data from one ValueResolve to another. This leads to  essential product-attributes such as the prices, categories, stock-levels, etc. are fetched over and over again since  they are used in multiple ValueResolvers.
Here quick example

We want to add these three price fields - standardPrice, salesPrice, netPrice - to the product document. The way to do it within SAP CC is to implement 3 independent ValueResolver, and each ValueResolver has to fetch the price-data of the product leading to redundant queries to the database.
By extending the out of the box DefaultIndexer and introducing a few custom classes and overarching context across all ValueResolver can be introduces which basically eliminates all the expensive repeated queries for the same data. 
One could think, the query-cache and entity-cache within SAP CC,...



On the GitHub-repo linked below I explain the technical implementation and provide all the code required for it.


# Technical Implementation

## Introducing the Context

1. First we extend DefaultSolrInputDocument so that it can hold a context, which is basically a HashMap:
[CustomSolrInputDocument.java](https://github.com/renatoh/SAPCommerceCloudSolrIndexingOptimization/blob/main/src/custom/solrfacetsearch/indexer/imp/CustomSolrInputDocument.java)

2. Next we introduce a generic way how attributes can be added to the context per type, here for the ProductModel since we index products. 
Generic Interface:
[SolrDocumentContextProvider.java](https://github.com/renatoh/SAPCommerceCloudSolrIndexingOptimization/blob/main/src/custom/solrfacetsearch/indexer/SolrDocumentContextProvider.java)


3. Here impleneting the interface above for the ProductModel. It is loading the priceInfo into the contet so that is available from the ValueResolver:
[CustomProductDocumentContextProvider.java](https://github.com/renatoh/SAPCommerceCloudSolrIndexingOptimization/blob/main/src/custom/solrfacetsearch/indexer/imp/CustomProductDocumentContextProvider.java)

4. Then we have to extend the DefaultIndexer to make sure your CustomSolrInputDocument is utilized and the SolrDocumentContextProviders are called to populate the attribure onto the context:
[CustomIndexer.java](https://github.com/renatoh/SAPCommerceCloudSolrIndexingOptimization/blob/main/src/custom/solrfacetsearch/indexer/imp/CustomIndexer.java)


## Accessing the Context

1. We introduce an abstract ValueResolver which provides a method for reading the attributes form the context:
[AbstractCustomProductValueResolver.java](https://github.com/renatoh/SAPCommerceCloudSolrIndexingOptimization/blob/main/src/custom/solrfacetsearch/resolver/product/imp/AbstractCustomProductValueResolver.java)

2. Now we can access to context from a concrete ValueResolver:
[CustomPriceAttributesValueResolver.java](https://github.com/renatoh/SAPCommerceCloudSolrIndexingOptimization/blob/main/src/custom/solrfacetsearch/resolver/product/imp/CustomPriceAttributesValueResolver.java)

## Spring Definition

All the Spring definition for the above classes is here:
[custom-spring.xml](https://github.com/renatoh/SAPCommerceCloudSolrIndexingOptimization/blob/main/resources/custom-spring.xml)





