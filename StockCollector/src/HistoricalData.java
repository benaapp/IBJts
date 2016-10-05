// Import Java utilities
import java.util.Vector;
// Import File writer
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// Import IB API
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.TagValue;
import com.ib.client.CommissionReport;
import com.ib.client.UnderComp;

/**
 * Created by lixiz on 10/3/16.
 */
public class HistoricalData implements EWrapper {
    // Keep track of the next Order ID
    private int nextOrderID = 0;
    private int reqId = 0;
    // The IB API Client Socket object
    private EClientSocket client = null;
    // port number
    private int port = 7496;
    // connect id
    private int connectId = 999;
    // write data into the file
    private String path = "";
    private File file;
    private FileWriter fileWriter;

    public void writeIntoFile(String str) {
        try {
            fileWriter.write(str);
        } catch(IOException e) {
            error(e);
        }
    }

    /**
     * get connection with the IB server
     */
    public void getConnection() {
        client = new EClientSocket (this);
        client.eConnect (null, port, connectId);
        try {
            while (! (client.isConnected()));
        }
        catch (Exception e) {
            error(e);
        }
    }

    /**
     * reqId should be uniq for each API call
     * @return
     */
    public int getReqId() {
        reqId++;
        return reqId;
    }

    public HistoricalData() {
        getConnection();

        // create a file writer
        /*
        try {
            file = new File(path);
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            error(e);
        }*/

        String symbol = "UVXY";   // symbol of stock
        // Use the format yyyymmdd hh:mm:ss tmz, where the time zone is allowed (optionally) after a space at the end.
        String endTime = "20120128 12:00:00";
        // format <Int> <type>; type: S (seconds), D (days), W (weeks), M (months), Y (years)
        String duration = "1 D";
        // 1,5,15,30 sec; 1,2,3,5,15,30 min; 1 hour; 1 day
        String barSize = "1 hour";
        // TRADES, MIDPOINT, BID, ASK, BID_ASK, HISTORICAL_VOLATILITY, OPTION_IMPLIED_VOLATILITY
        String whatToShow = "TRADES";
        // 0 - all; 1 - regular trading hours
        int useRTH = 0;
        // return date format 1 - yyyymmdd{space}{space}hh:mm:dd;
        //                    2 - long integer specifying the number of seconds since 1/1/1970 GMT
        int dateFormat = 1;

        // TODO:
        // for loop for the stock endTime to get the data
        // the endTime should be from 20120101 to now!

        getHistoricalData(symbol, endTime, duration, barSize, whatToShow, useRTH, dateFormat);

        /* Write into File
        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            error(e);
        }*/
    }

    public void getHistoricalData(String symbol, String endTime, String duration, String barSize, String whatToShow, int useRTH, int dateFormat) {
        Contract contract = ContractFactory.GenericStockContract(symbol);
        // Create a TagValue list for chartOptions
        Vector<TagValue> chartOptions = new Vector<TagValue>();
        // Make a call to start off data historical retrieval
        client.reqHistoricalData(getReqId(), contract, endTime, duration, barSize, whatToShow, useRTH, dateFormat, chartOptions);
    }

    /**
     * HistoricalData function get the data back and do whatever you want
     * @param reqId  The reqID passed in at the call to reqHistoricalData
     * @param date The date (and time) for the data bar
     * @param open the OHLC prices for the data bar.
     * @param high the OHLC prices for the data bar.
     * @param low the OHLC prices for the data bar.
     * @param close the OHLC prices for the data bar.
     * @param volume  The total volume of trades (or quoted size) that occurred during the data bar.
     * @param count The number of trades (or quotes) that occurred during the data bar.
     * @param WAP  The volume weighted average price (VWAP) during the bar.
     * @param hasGaps true if data used to create the bar has gaps in it.
     */
    public void historicalData(int reqId, String date, double open,
                               double high, double low, double close, int volume, int count,
                               double WAP, boolean hasGaps)
    {
        // Display Historical data
        try
        {
            if (!date.contains("finished")) {
                String str = date + " " + open + " " + high + " " + low + " " + close + " " + volume;
                System.out.println(str);
                // write Into File
                //writeIntoFile(str);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    //---------------- unimplemented methods --------------
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {}

    public void contractDetails(int reqId, ContractDetails contractDetails) {}

    public void contractDetailsEnd(int reqId) {}

    public void fundamentalData(int reqId, String data) {}

    public void bondContractDetails(ContractDetails contractDetails) {}

    public void contractDetails(ContractDetails contractDetails) {}

    public void currentTime(long time) {}

    public void displayGroupList(int requestId, String contraftInfo) {}

    public void displayGroupUpdated(int requestId, String contractInfo) {}

    public void verifyCompleted(boolean completed, String contractInfo) {}

    public void verifyMessageAPI(String message) {}

    public void execDetails(int orderId, Contract contract, Execution execution) {}

    public void execDetailsEnd(int reqId) {}

    public void managedAccounts(String accountsList) {}

    public void commissionReport(CommissionReport cr) {}

    public void position(String account, Contract contract, int pos, double avgCost) {}

    public void positionEnd() {}

    public void accountSummary(int reqId, String account, String tag, String value, String currency) {}

    public void accountSummaryEnd(int reqId) {}

    public void accountDownloadEnd(String accountName) {}

    public void openOrder(int orderId, Contract contract, Order order,
                          OrderState orderState) {}

    public void openOrderEnd() {}

    public void orderStatus(int orderId, String status, int filled,
                            int remaining, double avgFillPrice, int permId, int parentId,
                            double lastFillPrice, int clientId, String whyHeld) {}

    public void receiveFA (int faDataType, String xml) {}

    public void scannerData(int reqId, int rank,
                            ContractDetails contractDetails, String distance, String benchmark,
                            String projection, String legsStr) {}

    public void scannerDataEnd(int reqId) {}

    public void scannerParameters(String xml) {}

    public void tickPrice(int orderId, int field, double price,
                          int canAutoExecute) {}

    public void tickEFP(int symbolId, int tickType, double basisPoints,
                        String formattedBasisPoints, double impliedFuture, int holdDays,
                        String futureExpiry, double dividendImpact, double dividendsToExpiry) {}

    public void tickGeneric(int symbolId, int tickType, double value) {}

    public void tickOptionComputation( int tickerId, int field,
                                       double impliedVol, double delta, double optPrice,
                                       double pvDividend, double gamma, double vega,
                                       double theta, double undPrice) {}


    public void deltaNeutralValidation(int reqId, UnderComp underComp) {}

    public void updateAccountTime(String timeStamp) {}

    public void updateAccountValue(String key, String value, String currency,
                                   String accountName) {}

    public void updateMktDepth(int symbolId, int position, int operation,
                               int side, double price, int size) {}

    public void updateMktDepthL2(int symbolId, int position,
                                 String marketMaker, int operation, int side, double price, int size) {}

    public void updateNewsBulletin(int msgId, int msgType, String message,
                                   String origExchange) {}

    public void updatePortfolio(Contract contract, int position,
                                double marketPrice, double marketValue, double averageCost,
                                double unrealizedPNL, double realizedPNL, String accountName) {}

    public void marketDataType(int reqId, int marketDataType) {}

    public void tickSnapshotEnd(int tickerId) {}

    public void connectionClosed() {}

    public void realtimeBar(int reqId, long time, double open, double high,
                            double low, double close, long volume, double wap, int count) {}

    public void tickSize(int orderId, int field, int size) {}

    public void tickString(int orderId, int tickType, String value) {}

    //---------------------- Help functions ------------------

    public void error(Exception e) {
        // Print out a stack trace for the exception
        e.printStackTrace ();
    }

    public void error(String str) {
        // Print out the error message
        System.err.println (str);
    }

    public void error(int id, int errorCode, String errorMsg) {
        // Overloaded error event (from IB) with their own error
        // codes and messages
        System.err.println ("error: " + id + "," + errorCode + "," + errorMsg);
    }

    public void nextValidId (int orderId) {
        // Return the next valid OrderID
        nextOrderID = orderId;
    }

    public static void main (String args[])
    {
        try
        {
            // Create an instance
            // At this time a connection will be made
            // and the request for historical data will happen
            HistoricalData myData = new HistoricalData();
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }
    }

}