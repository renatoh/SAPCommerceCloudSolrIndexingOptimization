package custom.solrfacetsearch.resolver.product.imp;

import static custom.solrfacetsearch.indexer.imp.CustomProductDocumentContextProvider.PRICE_INFO_KEY;

import java.util.List;

import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;


public class CustomPriceAttributesValueResolver extends AbstractCustomProductValueResolver
{

	@Override
	protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, ProductModel productModel, ValueResolverContext<FeatureList, Object> valueResolverContext) throws FieldValueProviderException {
		
		List<PriceInformation> documentContextAttribute = getDocumentContextAttribute(inputDocument, PRICE_INFO_KEY);
		//add prices to inputDocument here
		
	}


}
