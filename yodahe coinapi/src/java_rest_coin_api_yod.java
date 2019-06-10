import java.sql.Timestamp;

public class java_rest_coin_api_yod {

    private static final String KEY = "YOUR_API_KEY_HERE";

    private static String asset_to_string(java_rest_coin_api.asset a) {
        if (a.get_name()!=null)
            return String.format("Asset (name = %s, id = %s, cryptocurrency = %s)", 
                a.get_name(), a.get_asset_id(), a.is_type_crypto() ? "yes" : "no");
        else
            return String.format("Asset (null name, id = %s, cryptocurrency = %s)", 
                a.get_asset_id(), a.is_type_crypto() ? "yes" : "no");
    }

    private static String exchange_to_string(java_rest_coin_api.exchange e) {
         return String.format("Exchange (name = %s, id = %s, website = %s)", 
                e.get_name(), e.get_exchange_id(), e.get_website());
    }

    private static String symbol_to_string(java_rest_coin_api.symbol s) {
        String result  = "";
        String additional = "";

        if (s.get_symbol_type() == java_rest_coin_api.SYMBOL_TYPE.SPOT) {
            result += "Spot symbol ";
        }
        else if (s.get_symbol_type() == java_rest_coin_api.SYMBOL_TYPE.FUTURES) {
            result += "Futures symbol ";
            additional += "\r\n   Delivery date: " + s.get_future_delivery_time().toString();
        }
        else if (s.get_symbol_type() == java_rest_coin_api.SYMBOL_TYPE.OPTION) {
            result += "Option symbol ";
            additional += "\r\n   Type of option: " + (s.get_option_type_is_call() ? "Call" : "Put") + ", ";
            additional += "Strike price: " + s.get_option_strike_price() + ", ";
            additional += "Conract Unit: " + s.get_option_contract_unit();

            if (s.get_option_exercise_style() == java_rest_coin_api.OPTION_EXERCISE_STYLE.EUROPEAN)
                additional += "\r\n   European exercise style";
            if (s.get_option_exercise_style() == java_rest_coin_api.OPTION_EXERCISE_STYLE.AMERICAN)
                additional += "\r\n   American exercise style";
            additional += "\r\n   Expiration time: " + s.get_option_expiration_time();
        }
        else {
            result += "Invalid symbol ";
        }

	result += "(symbol_id = " + s.get_symbol_id();
	result += ", exchange_id = " + s.get_exchange_id();
        result += ", " + s.get_asset_id_base() + "/" + s.get_asset_id_quote() + ")";
         
        return result + additional;
    }

    private static String exchange_rate_to_string(java_rest_coin_api.exchange_rate e) {
        return String.format("Exchange rate for %s/%s at %s is %.7f", e.get_asset_id_base(), e.get_asset_id_quote(), e.get_time().toString(), e.get_rate());
    }

    private static String period_to_string(java_rest_coin_api.period p) {
        return String.format("Period (seconds = %d, months = %d, unit count = %d, unit_name = %s), display name: %s", 
            p.get_length_seconds(), p.get_length_months(), p.get_unit_count(), p.get_unit_name(), p.get_display_name());
    }

    private static String timedata_to_string(java_rest_coin_api.timedata t) {
        return String.format("Timeserie (start: %s, end: %s, open: %s, close: %s)\r\n   Price (open: %.2f, low: %.2f, high: %.2f: close: %.2f)\r\n   Trade (Volume: %.7f, Count: %d)",
            t.get_time_period_start().toString(), t.get_time_period_end().toString(), t.get_time_open().toString(), t.get_time_close().toString(), 
            t.get_price_open(), t.get_price_low(), t.get_price_high(), t.get_price_close(), t.get_volume_traded(), t.get_trades_count());
    }

    private static String trade_to_string(java_rest_coin_api.trade t) {
        return String.format("Trade (symbol = %s, exchange time = %s, CoinAPI time: %s)\r\n   UUID = %s, price = %.2f, size = %.6f, taker side: %s", 
            t.get_symbol_id(), t.get_time_exchange(), t.get_time_coinapi(), t.get_uuid(), t.get_price(), t.get_size(), t.get_taker_side().toString());
    }

    private static String quote_with_trade_to_string(java_rest_coin_api.quote_with_trade q) {
        if (q.has_last_trade()) {
            return String.format("Quote (symbol = %s, exchange time = %s, CoinAPI time: %s)\r\n   Ask(Price = %.2f, Size = %.6f)\r\n   Bid(Price = %.2f, Size = %.6f)\r\n  Last %s", 
                q.get_symbol_id(), q.get_time_exchange(), q.get_time_coinapi(), q.get_ask_price(), q.get_ask_size(), q.get_bid_price(), q.get_bid_size(), 
                trade_to_string(q.get_last_trade()));
        }
        else {
            return String.format("Quote (symbol = %s, exchange time = %s, CoinAPI time: %s)\r\n   Ask(Price = %.2f, Size = %.6f)\r\n   Bid(Price = %.2f, Size = %.6f)", 
                q.get_symbol_id(), q.get_time_exchange(), q.get_time_coinapi(), q.get_ask_price(), q.get_ask_size(), q.get_bid_price(), q.get_bid_size());
        }
    }

    private static String quote_to_string(java_rest_coin_api.quote q) {
        return String.format("Quote (symbol = %s, exchange time = %s, CoinAPI time: %s)\r\n   Ask(Price = %.2f, Size = %.6f)\r\n   Bid(Price = %.2f, Size = %.6f)", 
            q.get_symbol_id(), q.get_time_exchange(), q.get_time_coinapi(), q.get_ask_price(), q.get_ask_size(), q.get_bid_price(), q.get_bid_size());
    }

    private static String asks_to_string(java_rest_coin_api.orderbook o) {
        String result = "";
        for (int i=0; i<o.get_asks_count(); i++) {
            result += String.format("\r\n   Ask(Price = %.2f, Size = %.6f)", o.get_ask(i).get_price(), o.get_ask(i).get_size());
        }        
        return result;
    }
    private static String bids_to_string(java_rest_coin_api.orderbook o) {
        String result = "";
        for (int i=0; i<o.get_bids_count(); i++) {
            result += String.format("\r\n   Bid(Price = %.2f, Size = %.6f)", o.get_bid(i).get_price(), o.get_bid(i).get_size());
        }
        return result;
    }

    private static String orderbook_to_string(java_rest_coin_api.orderbook o) {
        return String.format("Orderbook (symbol = %s, exchange time = %s, CoinAPI time: %s)\r\n   Asks:%s\r\n   Bids:%s", 
             o.get_symbol_id(), o.get_time_exchange(), o.get_time_coinapi(), asks_to_string(o), bids_to_string(o));
    }

   

    private static void test_quotes_get_latest_data() throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get latest quotes started");
        java_rest_coin_api.quote[] result = c.quotes_get_latest_data();
        if (result!=null) {
            for(java_rest_coin_api.quote t : result)
                System.out.println(quote_to_string(t));
        }
	System.out.println("Get latest quotes finished");
        System.out.println();
    }

    private static void test_quotes_get_latest_data(int limit) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get latest quotes started");
        java_rest_coin_api.quote[] result = c.quotes_get_latest_data(limit);
        if (result!=null) {
            for(java_rest_coin_api.quote t : result)
                System.out.println(quote_to_string(t));
        }
	System.out.println("Get latest quotes finished");
        System.out.println();
    }

    private static void test_quotes_get_latest_data(String symbol_id) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get latest quotes started");
        java_rest_coin_api.quote[] result = c.quotes_get_latest_data(symbol_id);
        if (result!=null) {
            for(java_rest_coin_api.quote t : result)
                System.out.println(quote_to_string(t));
        }
	System.out.println("Get latest quotes finished");
        System.out.println();
    }

    private static void test_quotes_get_latest_data(String symbol_id, int limit) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get latest quotes started");
        java_rest_coin_api.quote[] result = c.quotes_get_latest_data(symbol_id, limit);
        if (result!=null) {
            for(java_rest_coin_api.quote t : result)
                System.out.println(quote_to_string(t));
        }
	System.out.println("Get latest quotes finished");
        System.out.println();
    }

    private static void test_quotes_get_historical_data(String symbol_id, Timestamp time_start) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get historical quotes started");
        java_rest_coin_api.quote[] result = c.quotes_get_historical_data(symbol_id, time_start);
        if (result!=null) {
            for(java_rest_coin_api.quote t : result)
                System.out.println(quote_to_string(t));
        }
	System.out.println("Get historical quotes finished");
        System.out.println();
    }

    private static void test_quotes_get_historical_data(String symbol_id, Timestamp time_start, int limit) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get historical quotes started");
        java_rest_coin_api.quote[] result = c.quotes_get_historical_data(symbol_id, time_start, limit);
        if (result!=null) {
            for(java_rest_coin_api.quote t : result)
                System.out.println(quote_to_string(t));
        }
	System.out.println("Get historical quotes finished");
        System.out.println();
    }

    private static void test_quotes_get_historical_data(String symbol_id, Timestamp time_start, Timestamp time_end) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get historical quotes started");
        java_rest_coin_api.quote[] result = c.quotes_get_historical_data(symbol_id, time_start, time_end);
        if (result!=null) {
            for(java_rest_coin_api.quote t : result)
                System.out.println(quote_to_string(t));
        }
	System.out.println("Get historical quotes finished");
        System.out.println();
    }

    private static void test_quotes_get_historical_data(String symbol_id, Timestamp time_start, Timestamp time_end, int limit) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get historical quotes started");
        java_rest_coin_api.quote[] result = c.quotes_get_historical_data(symbol_id, time_start, time_end, limit);
        if (result!=null) {
            for(java_rest_coin_api.quote t : result)
                System.out.println(quote_to_string(t));
        }
	System.out.println("Get historical quotes finished");
        System.out.println();
    }
    
    private static void test_orderbooks_get_for_all_symbols() throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
        System.out.println("Get current orderbooks started");
	java_rest_coin_api.orderbook[] result = c.orderbooks_get_for_all_symbols();
        if (result!=null) {
            for(java_rest_coin_api.orderbook o : result)
                System.out.println(orderbook_to_string(o));
        }

	System.out.println("Get current orderbooks finished");
        System.out.println();
    }

    private static void test_orderbooks_get_for_symbol(String symbol_id) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
        System.out.println("Get current orderbook started");
	java_rest_coin_api.orderbook result = c.orderbooks_get_for_symbol(symbol_id);
        if (result!=null) {
            System.out.println(orderbook_to_string(result));
        }

	System.out.println("Get current orderbook finished");
        System.out.println();
    }

    private static void test_orderbooks_get_latest_data(String symbol_id) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
        System.out.println("Get latest orderbooks started");
	java_rest_coin_api.orderbook[] result = c.orderbooks_get_latest_data(symbol_id);
        if (result!=null) {
            for(java_rest_coin_api.orderbook o : result)
                System.out.println(orderbook_to_string(o));
        }

	System.out.println("Get latest orderbooks finished");
        System.out.println();
    }
    
    private static void test_orderbooks_get_latest_data(String symbol_id, int limit) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
        System.out.println("Get latest orderbooks started");
	java_rest_coin_api.orderbook[] result = c.orderbooks_get_latest_data(symbol_id, limit);
        if (result!=null) {
            for(java_rest_coin_api.orderbook o : result)
                System.out.println(orderbook_to_string(o));
        }

	System.out.println("Get latest orderbooks finished");
        System.out.println();
    }

    private static void test_orderbooks_get_historical_data(String symbol_id, Timestamp time_start) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get historical orderbooks started");
        java_rest_coin_api.orderbook[] result = c.orderbooks_get_historical_data(symbol_id, time_start);
        if (result!=null) {
            for(java_rest_coin_api.orderbook o : result)
                System.out.println(orderbook_to_string(o));
        }
	System.out.println("Get historical orderbooks finished");
        System.out.println();
    }

    private static void test_orderbooks_get_historical_data(String symbol_id, Timestamp time_start, int limit) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get historical orderbooks started");
        java_rest_coin_api.orderbook[] result = c.orderbooks_get_historical_data(symbol_id, time_start, limit);
        if (result!=null) {
            for(java_rest_coin_api.orderbook o : result)
                System.out.println(orderbook_to_string(o));
        }
	System.out.println("Get historical orderbooks finished");
        System.out.println();
    }

    private static void test_orderbooks_get_historical_data(String symbol_id, Timestamp time_start, Timestamp time_end) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get historical orderbooks started");
        java_rest_coin_api.orderbook[] result = c.orderbooks_get_historical_data(symbol_id, time_start, time_end);
        if (result!=null) {
            for(java_rest_coin_api.orderbook o : result)
                System.out.println(orderbook_to_string(o));
        }
	System.out.println("Get historical orderbooks finished");
        System.out.println();
    }

    private static void test_orderbooks_get_historical_data(String symbol_id, Timestamp time_start, Timestamp time_end, int limit) throws java_rest_coin_api.exception {
	java_rest_coin_api c = new java_rest_coin_api(KEY);
	System.out.println("Get historical orderbooks started");
        java_rest_coin_api.orderbook[] result = c.orderbooks_get_historical_data(symbol_id, time_start, time_end, limit);
        if (result!=null) {
            for(java_rest_coin_api.orderbook o : result)
                System.out.println(orderbook_to_string(o));
        }
	System.out.println("Get historical orderbooks finished");
        System.out.println();
    }

    public static void main(String[] args) {

        try {
            // https://rest.coinapi.io/v1/exchanges?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_metadata_list_all_exchanges();

            // https://rest.coinapi.io/v1/assets?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_metadata_list_all_exchanges();

            // https://rest.coinapi.io/v1/symbols?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_metadata_list_all_exchanges();

            // https://rest.coinapi.io/v1/exchangerate/BTC/USD?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_get_exchange_rate("BTC", "USD");

            // https://rest.coinapi.io/v1/exchangerate/BTC/USD?time=2016-11-01T22:08:41Z&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_get_exchange_rate("BTC", "USD");

            // https://rest.coinapi.io/v1/exchangerate/BTC?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_get_all_exchange_rates("BTC");

            // https://rest.coinapi.io/v1/ohlcv/periods?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_ohlcv_list_all_periods();

            // https://rest.coinapi.io/v1/ohlcv/BITSTAMP_SPOT_BTC_USD/latest?period_id=1MIN&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_ohlcv_get_latest_timeseries("BITSTAMP_SPOT_BTC_USD", java_rest_coin_api.PERIOD_IDENTIFIER._1MIN);

            // https://rest.coinapi.io/v1/ohlcv/BITSTAMP_SPOT_BTC_USD/latest?period_id=1MIN&limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_ohlcv_get_latest_timeseries("BITSTAMP_SPOT_BTC_USD", java_rest_coin_api.PERIOD_IDENTIFIER._1MIN, 5);

            // https://rest.coinapi.io/v1/ohlcv/BITSTAMP_SPOT_BTC_USD/history?period_id=1MIN&time_start=2016-11-01T22:08:41Z&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_ohlcv_get_historical_timeseries("BITSTAMP_SPOT_BTC_USD", java_rest_coin_api.PERIOD_IDENTIFIER._1MIN, new Timestamp(116,10,1,22,8,41,0));

            // https://rest.coinapi.io/v1/ohlcv/BITSTAMP_SPOT_BTC_USD/history?period_id=1MIN&time_start=2016-11-01T22:08:41Z&time_end=2017-06-25T10:24:07-05:00&limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_ohlcv_get_historical_timeseries("BITSTAMP_SPOT_BTC_USD", java_rest_coin_api.PERIOD_IDENTIFIER._1MIN, new Timestamp(116,10,1,22,8,41,0), new Timestamp(117,5,25,10,24,7,0), 5);

            // https://rest.coinapi.io/v1/ohlcv/BITSTAMP_SPOT_BTC_USD/history?period_id=1MIN&time_start=2016-11-01T22:08:41Z&time_end=2017-06-25T10:24:07-05:00&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_ohlcv_get_historical_timeseries("BITSTAMP_SPOT_BTC_USD", java_rest_coin_api.PERIOD_IDENTIFIER._1MIN, new Timestamp(116,10,1,22,8,41,0), new Timestamp(117,5,25,10,24,7,0));

            // https://rest.coinapi.io/v1/ohlcv/BITSTAMP_SPOT_BTC_USD/history?period_id=1MIN&time_start=2016-11-01T22:08:41Z&limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_ohlcv_get_historical_timeseries("BITSTAMP_SPOT_BTC_USD", java_rest_coin_api.PERIOD_IDENTIFIER._1MIN, new Timestamp(116,10,1,22,8,41,0), 5);

            // https://rest.coinapi.io/v1/trades/latest?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_trades_get_latest_data();

            // https://rest.coinapi.io/v1/trades/latest?limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_trades_get_latest_data(5);

            // https://rest.coinapi.io/v1/trades/BITSTAMP_SPOT_BTC_USD/latest?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_trades_get_latest_data("BITSTAMP_SPOT_BTC_USD");

            // https://rest.coinapi.io/v1/trades/BITSTAMP_SPOT_BTC_USD/latest?limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_trades_get_latest_data("BITSTAMP_SPOT_BTC_USD", 5);

            // https://rest.coinapi.io/v1/trades/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_trades_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(2016,10,1,22,8,41,0));

            // https://rest.coinapi.io/v1/trades/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_trades_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(2016,10,1,22,8,41,0), 5);

            // https://rest.coinapi.io/v1/trades/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&time_end=2017-06-25T10:24:08-05:00&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_trades_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(2016,10,1,22,8,41,0), new Timestamp(2017,5,25,10,24,8,0));

            // https://rest.coinapi.io/v1/trades/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&time_end=2017-06-25T10:24:08-05:00&limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_trades_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(2016,10,1,22,8,41,0), new Timestamp(2017,5,25,10,24,8,0), 5);

            // https://rest.coinapi.io/v1/quotes/current?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_quotes_get_for_all_symbols();

            // https://rest.coinapi.io/v1/quotes/BITSTAMP_SPOT_BTC_USD/current?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_quotes_get_for_symbol("BITSTAMP_SPOT_BTC_USD");

            // https://rest.coinapi.io/v1/quotes/latest?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_quotes_get_latest_data();

            // https://rest.coinapi.io/v1/quotes/latest?limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_quotes_get_latest_data(5);

            // https://rest.coinapi.io/v1/quotes/BITSTAMP_SPOT_BTC_USD/latest?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_quotes_get_latest_data("BITSTAMP_SPOT_BTC_USD");

            // https://rest.coinapi.io/v1/quotes/BITSTAMP_SPOT_BTC_USD/latest?limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_quotes_get_latest_data("BITSTAMP_SPOT_BTC_USD", 5);

            // https://rest.coinapi.io/v1/quotes/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_quotes_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(116,10,1,22,8,41,0));

            // https://rest.coinapi.io/v1/quotes/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_quotes_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(116,10,1,22,8,41,0), 5);

            // https://rest.coinapi.io/v1/quotes/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&time_end=2017-06-25T10:24:09-05:00&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_quotes_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(116,10,1,22,8,41,0), new Timestamp(117,5,25,10,24,9,0));

            // https://rest.coinapi.io/v1/quotes/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&time_end=2017-06-25T10:24:09-05:00&limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_quotes_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(116,10,1,22,8,41,0), new Timestamp(117,5,25,10,24,9,0), 5);

            // https://rest.coinapi.io/v1/orderbooks/current?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_orderbooks_get_for_all_symbols();

            // https://rest.coinapi.io/v1/orderbooks/BITSTAMP_SPOT_BTC_USD/current?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_orderbooks_get_for_symbol("BITSTAMP_SPOT_BTC_USD");

            // https://rest.coinapi.io/v1/orderbooks/BITSTAMP_SPOT_BTC_USD/latest?apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_orderbooks_get_latest_data("BITSTAMP_SPOT_BTC_USD");

            // https://rest.coinapi.io/v1/orderbooks/BITSTAMP_SPOT_BTC_USD/latest?limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_orderbooks_get_latest_data("BITSTAMP_SPOT_BTC_USD", 5);

            // https://rest.coinapi.io/v1/orderbooks/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_orderbooks_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(116,10,1,22,8,41,0));

            // https://rest.coinapi.io/v1/orderbooks/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_orderbooks_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(116,10,1,22,8,41,0), 5);

            // https://rest.coinapi.io/v1/orderbooks/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&time_end=2017-06-25T10:24:09-05:00&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_orderbooks_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(116,10,1,22,8,41,0), new Timestamp(117,5,25,10,24,9,0));

            // https://rest.coinapi.io/v1/orderbooks/BITSTAMP_SPOT_BTC_USD/history?time_start=2016-11-01T22:08:41Z&time_end=2017-06-25T10:24:09-05:00&limit=5&apikey=73034021-0EBC-493D-8A00-E0F138111F41
            test_orderbooks_get_historical_data("BITSTAMP_SPOT_BTC_USD", new Timestamp(116,10,1,22,8,41,0), new Timestamp(117,5,25,10,24,9,0), 5);
        }
        catch(java_rest_coin_api.exception ex) {
            System.err.println("CoinAPI Exception: " + ex.getMessage());
        }
        catch(Exception ex) {
            System.err.println("Generic exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

	private static void test_get_all_exchange_rates(String string) {
		// TODO Auto-generated method stub
		
	}

	private static void test_get_exchange_rate(String string, String string2) {
		// TODO Auto-generated method stub
		
	}

	private static void test_metadata_list_all_exchanges() {
		// TODO Auto-generated method stub
		
	}
}