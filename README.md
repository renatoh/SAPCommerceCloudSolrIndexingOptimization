Work in Progress!!

Supercharging Solr-Indexing in SAP Commerce Cloud aka Hybris

Many SAP Commerce Cloud (called SAP CC) deployments are building the Solr product index from scratch every 24h in order to have reflect the latest prices, stock levels, etc in the product search. These job often run for hours and are rebuilding  indexes with 100k+ product documents in it.
During the time of indexing most if not all the data pushed to the index is fetched from the database SAP CC is running on. This puts a lot of pressure onto the DB and very often the queries to the database fetching all the product attribute becomes the bottleneck of the Solr-indexing Job.


Playing around with some settings e.g. increasing the batch-size and thread pool-size for the worker thread is sometimes shorting the indexing time but it cannot overcome one fundamental flaw within the SAP CC’s Solr-integration and this issue is the following:
All the business logic required to fetch and calculate the attributes for the Solr-documents has to be put within ValueResolver (successor of FieldValueProvider). Every attribute requiring more business logic than what fits reasonably within a Spring-Expression, requires its very own ValueResolver, therefore dozens of them will be created.
These  ValueResolver are executed independently of each other, there is no overarching context or the option to share data from one ValueResolve to another. This leads to  essential product-attributes such as the prices, categories, stock-levels, etc. are fetched over and over again since  they are used in multiple ValueResolvers.
Here quick example

We want to add these three price-fields price fields - standardPrice, salesPrice, netPrice - to the product document. The way  to do it within SAP CC is to implement 3 independent ValueResolver, and each ValueResolver has to fetch the price rows of the product leading to redundant queries to the database.
By extending the out of the box DefaultIndexer and introducing a few custom class and overarching context across all ValueResolver execution for a given product can be introduces which basically eliminates all the expensive repeated fetching of the same data from the database. 

On the GitHub-repo linked below I explain the technical implementation and provide all the code required for it.
