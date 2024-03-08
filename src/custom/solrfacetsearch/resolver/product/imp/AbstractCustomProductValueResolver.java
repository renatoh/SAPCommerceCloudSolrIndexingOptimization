package custom.solrfacetsearch.resolver.product.imp;

import custom.solrfacetsearch.indexer.imp.CustomSolrInputDocument;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

public abstract class AbstractCustomProductValueResolver extends AbstractValueResolver<ProductModel, FeatureList, Object>
{
	protected <T> T getDocumentContextAttribute(final InputDocument document, String attributeKey)
	{
		if(document instanceof CustomSolrInputDocument)
		{
			Object o = ((CustomSolrInputDocument) document).getIndexDocumentContext().get(attributeKey);
			if(o.getClass().equals(CustomSolrInputDocument.class))
			{
				return (T) o;
			}
				
		}
		throw new IllegalArgumentException("Document must be of type CustomSolrInputDocument");
	}
}
