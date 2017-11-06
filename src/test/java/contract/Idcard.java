package contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>, or {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version 2.3.1.
 */
public final class Idcard extends Contract {
    private static final String BINARY = "0x60606040526000600355341561001457600080fd5b336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610a6b806100636000396000f300606060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630b833b0b1461007d57806383841e0c146100a65780638da5cb5b14610146578063a6ec03b21461019b578063ebdf86ca14610271578063f62ab79614610311575b600080fd5b341561008857600080fd5b610090610382565b6040518082815260200191505060405180910390f35b34156100b157600080fd5b610144600480803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190505061038c565b005b341561015157600080fd5b610159610574565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156101a657600080fd5b6101f6600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610599565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561023657808201518184015260208101905061021b565b50505050905090810190601f1680156102635780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561027c57600080fd5b61030f600480803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919050506106ac565b005b341561031c57600080fd5b61036c600480803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610911565b6040518082815260200191505060405180910390f35b6000600354905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156103e757600080fd5b806001836040518082805190602001908083835b60208310151561042057805182526020820191506020810190506020830392506103fb565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390209080519060200190610466929190610986565b507f0a2724eb6606f91e6655f6afd84567038b38fcf615847d5d6a5a90a2df943b9f8282604051808060200180602001838103835285818151815260200191508051906020019080838360005b838110156104ce5780820151818401526020810190506104b3565b50505050905090810190601f1680156104fb5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610534578082015181840152602081019050610519565b50505050905090810190601f1680156105615780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a15050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6105a1610a06565b6001826040518082805190602001908083835b6020831015156105d957805182526020820191506020810190506020830392506105b4565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106a05780601f10610675576101008083540402835291602001916106a0565b820191906000526020600020905b81548152906001019060200180831161068357829003601f168201915b50505050509050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561070757600080fd5b806001836040518082805190602001908083835b602083101515610740578051825260208201915060208101905060208303925061071b565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390209080519060200190610786929190610986565b5060016002836040518082805190602001908083835b6020831015156107c1578051825260208201915060208101905060208303925061079c565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020819055506001600354016003819055507f725c776684e1497cb42062dd8c2a11204ea93c9479219508e35abff633698f4b8282604051808060200180602001838103835285818151815260200191508051906020019080838360005b8381101561086b578082015181840152602081019050610850565b50505050905090810190601f1680156108985780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156108d15780820151818401526020810190506108b6565b50505050905090810190601f1680156108fe5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a15050565b60006002826040518082805190602001908083835b60208310151561094b5780518252602082019150602081019050602083039250610926565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020549050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106109c757805160ff19168380011785556109f5565b828001600101855582156109f5579182015b828111156109f45782518255916020019190600101906109d9565b5b509050610a029190610a1a565b5090565b602060405190810160405280600081525090565b610a3c91905b80821115610a38576000816000905550600101610a20565b5090565b905600a165627a7a723058202669c52921075d4a344143a13d4651140c6bf8bd8560e455386b5ef0845b12da0029";

    private Idcard(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Idcard(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<AddEventResponse> getAddEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Add", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<AddEventResponse> responses = new ArrayList<AddEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            AddEventResponse typedResponse = new AddEventResponse();
            typedResponse._idCardNo = (Utf8String) eventValues.getNonIndexedValues().get(0);
            typedResponse._signedData = (Utf8String) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddEventResponse> addEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Add", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, AddEventResponse>() {
            @Override
            public AddEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                AddEventResponse typedResponse = new AddEventResponse();
                typedResponse._idCardNo = (Utf8String) eventValues.getNonIndexedValues().get(0);
                typedResponse._signedData = (Utf8String) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public List<ChangeEventResponse> getChangeEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Change", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<ChangeEventResponse> responses = new ArrayList<ChangeEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            ChangeEventResponse typedResponse = new ChangeEventResponse();
            typedResponse._idCardNo = (Utf8String) eventValues.getNonIndexedValues().get(0);
            typedResponse._signedData = (Utf8String) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ChangeEventResponse> changeEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Change", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ChangeEventResponse>() {
            @Override
            public ChangeEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                ChangeEventResponse typedResponse = new ChangeEventResponse();
                typedResponse._idCardNo = (Utf8String) eventValues.getNonIndexedValues().get(0);
                typedResponse._signedData = (Utf8String) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Future<Uint256> cardNumOf() {
        Function function = new Function("cardNumOf", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> change(Utf8String _idCardNo, Utf8String _signedData) {
        Function function = new Function("change", Arrays.<Type>asList(_idCardNo, _signedData), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Address> owner() {
        Function function = new Function("owner", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Utf8String> idCardOf(Utf8String _idCardNo) {
        Function function = new Function("idCardOf", 
                Arrays.<Type>asList(_idCardNo), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> add(Utf8String _idCardNo, Utf8String _signedData) {
        Function function = new Function("add", Arrays.<Type>asList(_idCardNo, _signedData), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> stateOf(Utf8String _idCardNo) {
        Function function = new Function("stateOf", 
                Arrays.<Type>asList(_idCardNo), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public static Future<Idcard> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(Idcard.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static Future<Idcard> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(Idcard.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static Idcard load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Idcard(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Idcard load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Idcard(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class AddEventResponse {
        public Utf8String _idCardNo;

        public Utf8String _signedData;
    }

    public static class ChangeEventResponse {
        public Utf8String _idCardNo;

        public Utf8String _signedData;
    }
}
