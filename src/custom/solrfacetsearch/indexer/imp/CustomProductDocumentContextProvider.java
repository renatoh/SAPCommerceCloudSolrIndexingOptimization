package custom.solrfacetsearch.indexer.imp;

import java.util.List;
import java.util.Map;


import custom.solrfacetsearch.indexer.SolrDocumentContextProvider;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;

/***
 * Provides data such as stock-level, priceInfo, etc. to all ValueResolver used to build a document for a ProductModel 
 */
public class CustomProductDocumentContextProvider implements SolrDocumentContextProvider<ProductModel>
{

	public static final String PRICE_INFO_KEY = "priceInfo";
	final private PriceService priceService;




    public CustomProductDocumentContextProvider(PriceService priceService) {
        this.priceService = priceService;
    }


    @Override
	public void addDocumentContext(Map<String, Object> context, ProductModel product, IndexerBatchContext batchContext)
	{
		List<PriceInformation> priceInformationsForProduct = priceService.getPriceInformationsForProduct(product);
		context.put(PRICE_INFO_KEY, priceInformationsForProduct);
	}

}
