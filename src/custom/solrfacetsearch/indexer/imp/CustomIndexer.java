package custom.solrfacetsearch.indexer.imp;

import java.util.Map;

import custom.solrfacetsearch.indexer.SolrDocumentContextProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;


import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexer;

public class CustomIndexer extends DefaultIndexer
{
	private final Map<String, SolrDocumentContextProvider<ItemModel>> contextProvidersByType;
	
	private static final Logger LOG = LogManager.getLogger(CustomIndexer.class);

	public CustomIndexer(final Map<String, SolrDocumentContextProvider<ItemModel>> contextProvidersByType)
	{
		this.contextProvidersByType = contextProvidersByType;
	}

	/**
	 * Resolves a document context for a SolrInputDocument and saves it on the document. This context is then available to all value-resolver
	 * Unfortunately the code of the super-method had to be copied in oder to be able to add the document-context to  SolrInputDocument
	 */
	
	protected SolrInputDocument createInputDocument(ItemModel model, IndexConfig indexConfig, IndexedType indexedType) throws FieldValueProviderException
	{
		long t1 = System.currentTimeMillis();

		this.validateCommonRequiredParameters(model, indexConfig, indexedType);
		IndexerBatchContext batchContext = this.getIndexerBatchContextFactory().getContext();
		SolrInputDocument doc = new SolrInputDocument();
		CustomSolrInputDocument wrappedDoc = this.createWrappedDocument(batchContext, doc);

		//we are adding context to the document, so that multiple value-resolver can make use of data fetched with a single call.
		SolrDocumentContextProvider<ItemModel> contextProvider = contextProvidersByType.get(batchContext.getIndexedType().getCode());
		if(contextProvider != null)
		{
			contextProvider.addDocumentContext(wrappedDoc.getIndexDocumentContext(), model, batchContext);
		}                                                            

		doc.addField("indexOperationId", batchContext.getIndexOperationId());
		this.addCommonFields(doc, batchContext, model);
		this.addIndexedPropertyFields(wrappedDoc, batchContext, model);
		this.addIndexedTypeFields(wrappedDoc, batchContext, model);

		batchContext.getInputDocuments().add(wrappedDoc);

		LOG.info("Time taken to build SolrInputDocument[{}] for {} is: {} ms", doc.get("id"), model.getItemtype(), System.currentTimeMillis() - t1);
		return doc;
	}

	protected CustomSolrInputDocument createWrappedDocument(IndexerBatchContext batchContext, SolrInputDocument delegate)
	{
		return new CustomSolrInputDocument(delegate, batchContext, this.getFieldNameProvider(), this.getRangeNameProvider());
	}

}
