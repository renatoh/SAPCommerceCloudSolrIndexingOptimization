package custom.solrfacetsearch.indexer.imp;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;

import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultSolrInputDocument;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.RangeNameProvider;

public class CustomSolrInputDocument extends DefaultSolrInputDocument
{

	public CustomSolrInputDocument(final SolrInputDocument delegate, final IndexerBatchContext batchContext, final FieldNameProvider fieldNameProvider, final RangeNameProvider rangeNameProvider)
	{
		super(delegate, batchContext, fieldNameProvider, rangeNameProvider);
	}
	
	private Map<String, Object> indexDocumentContext = new HashMap<>();
	
	public void startDocument() {
  }

  public void endDocument() {
  }

	public Map<String, Object> getIndexDocumentContext()
	{
		return indexDocumentContext;
	}

}
