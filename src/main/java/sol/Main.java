package sol;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple9;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/hyperledger-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.12.3.
 */
@SuppressWarnings("rawtypes")
public class Main extends Contract {
    public static final String BINARY = "6080604052348015600e575f80fd5b50600180546001600160a01b03191633179055610f668061002e5f395ff3fe608060405234801561000f575f80fd5b506004361061004a575f3560e01c8063608580141461004e578063a7c98b7c14610063578063e8a0348714610076578063fc64094914610089575b5f80fd5b61006161005c366004610cbb565b610152565b005b610061610071366004610d4c565b61042f565b610061610084366004610dab565b6105b5565b6100f5610097366004610ddb565b5f6020818152928152604080822090935290815220805460018201546002830154600384015460048501546005860154600687015460078801546008909801546001600160a01b039788169896979596949593949390921692909189565b604080516001600160a01b039a8b16815260208101999099528801969096526060870194909452608086019290925290941660a084015260c083019390935260e08201929092526101008101919091526101200160405180910390f35b6001546001600160a01b031633146101a05760405162461bcd60e51b815260206004820152600c60248201526b34b9903737ba1030b236b4b760a11b60448201526064015b60405180910390fd5b6001600160a01b038581165f90815260208181526040808320888452909152902054166101df5760405162461bcd60e51b815260040161019790610e03565b6001600160a01b0385165f90815260208181526040808320878452825280832086845260098101909252909120805460141461022d5760405162461bcd60e51b815260040161019790610e33565b5f805b8481101561028d573686868381811061024b5761024b610e7c565b6060029190910191505f905061026660208301358335610ea4565b905060408201356102778286610ec1565b6102819190610ec1565b93505050600101610230565b505f83600701548261029f9190610ed4565b90505f8185600801546102b29190610ec1565b90505f5b600685015481101561035d575f8560060182815481106102d8576102d8610e7c565b5f918252602091829020604080516060810182526003909302909101805483526001810154938301849052600201546001600160a01b031690820152915083900361035457604001516003860180546001600160a01b0319166001600160a01b039092169190911790556002850182905543600486015561035d565b506001016102b6565b506004850154156103895760058501546004860154610389918c916001600160a01b0390911690610852565b600380850154908601546103aa918c916001600160a01b0390911690610852565b601e80855560058501819055600285015460038601546004870154604080519384526001600160a01b03928316602085015283015260608201839052608082019290925289918b91908d16907f1d22384f2d811831c8f0e41b6fc9041323cb3b6d9ff4ccc953a03172333eab929060a00160405180910390a450505050505050505050565b6001546001600160a01b031633146104785760405162461bcd60e51b815260206004820152600c60248201526b34b9903737ba1030b236b4b760a11b6044820152606401610197565b6001600160a01b038781165f908152602081815260408083208a845290915290205416156104b85760405162461bcd60e51b815260040161019790610e03565b6001600160a01b038781165f818152602081815260408083208b845282529182902080546001600160a01b031990811685178255606460018301819055600383018c9055600483018b9055600583018054909216968a169687179091556006820188905560078201879055629896816008830181905584519182529281018b905292830189905260608301949094526080820186905260a0820185905260c08201528891907fd83b772d48e2698aa772f59036cd61f059e3487aee81cf90c6b4934a6b6f79479060e00160405180910390a35f8160010154600161059c9190610ec1565b90506105aa89898385610a21565b505050505050505050565b6001600160a01b038381165f90815260208181526040808320868452909152902054166105f45760405162461bcd60e51b815260040161019790610e03565b6001600160a01b0383165f90815260208181526040808320858452825280832060028101548085526009820190935292208054600a146106465760405162461bcd60e51b815260040161019790610e33565b6007830154600682015461065b908690610ec1565b11156106a95760405162461bcd60e51b815260206004820152601f60248201527f4d6178696d756d206e756d626572206f662070617274696369706174696f6e006044820152606401610197565b5f8360060154856106ba9190610ea4565b84549091506106d4906001600160a01b0316333084610ac7565b5f5b858110156107c557600683015460088601545f916106f391610ec1565b604080516060808201835260068a810180548452602080850187815233868801818152948d018054600180820183555f92835291859020985160039091029098019788559151918701919091559251600290950180546001600160a01b0319166001600160a01b03968716179055905485519283529082018d90524394820194909452908101849052608081019290925291925086918a91908c16907fdda4586f27d561e4eaefb046f75f491b630963d6a5b065db42228203bc85a2579060a00160405180910390a4506001016106d6565b508082600101546107d69190610ec1565b60018301556007840154600683015403610849578286886001600160a01b03167f8af37bd3eec32d910fc984ac1d526a0087425d2cecc9313ae9bfdae7ff80537f4360405161082791815260200190565b60405180910390a4601482556108498787610843866001610ec1565b87610a21565b50505050505050565b6040516370a0823160e01b815230600482015283905f906001600160a01b038316906370a0823190602401602060405180830381865afa158015610898573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906108bc9190610ef3565b90508281101561090e5760405162461bcd60e51b815260206004820152601d60248201527f496e73756666696369656e7420636f6e74726163742062616c616e63650000006044820152606401610197565b60405163a9059cbb60e01b81526001600160a01b038581166004830152602482018590525f919084169063a9059cbb906044016020604051808303815f875af115801561095d573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906109819190610f0a565b9050806109c25760405162461bcd60e51b815260206004820152600f60248201526e151c985b9cd9995c8819985a5b1959608a1b6044820152606401610197565b846001600160a01b0316306001600160a01b0316876001600160a01b03167f1288af6a21bcbf8578557e4ee7c0be88a2be953a252ed25850019ea232b1a6bb87604051610a1191815260200190565b60405180910390a4505050505050565b60028181018390555f838152600983016020908152604091829020600a8082556005820181905560018201549482015460038301546004840154865184815295860197909752948401526001600160a01b039384166060840152608083019490945260a082019390935284918691908816907fe912b77d882c59c6d7d9a1ddd23f50fdb56d9b4bdb9442593fa96e6a8c2795ea9060c00160405180910390a45050505050565b604051636eb1769f60e11b81526001600160a01b03848116600483015230602483015285915f9183169063dd62ed3e90604401602060405180830381865afa158015610b15573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610b399190610ef3565b905082811015610b845760405162461bcd60e51b8152602060048201526016602482015275496e73756666696369656e7420616c6c6f77616e636560501b6044820152606401610197565b6040516323b872dd60e01b81526001600160a01b0386811660048301528581166024830152604482018590525f91908416906323b872dd906064016020604051808303815f875af1158015610bdb573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610bff9190610f0a565b905080610c405760405162461bcd60e51b815260206004820152600f60248201526e151c985b9cd9995c8819985a5b1959608a1b6044820152606401610197565b846001600160a01b0316866001600160a01b0316886001600160a01b03167f1288af6a21bcbf8578557e4ee7c0be88a2be953a252ed25850019ea232b1a6bb87604051610c8f91815260200190565b60405180910390a450505050505050565b80356001600160a01b0381168114610cb6575f80fd5b919050565b5f805f805f60808688031215610ccf575f80fd5b610cd886610ca0565b94506020860135935060408601359250606086013567ffffffffffffffff811115610d01575f80fd5b8601601f81018813610d11575f80fd5b803567ffffffffffffffff811115610d27575f80fd5b886020606083028401011115610d3b575f80fd5b959894975092955050506020019190565b5f805f805f805f60e0888a031215610d62575f80fd5b610d6b88610ca0565b9650602088013595506040880135945060608801359350610d8e60808901610ca0565b9699959850939692959460a0840135945060c09093013592915050565b5f805f60608486031215610dbd575f80fd5b610dc684610ca0565b95602085013595506040909401359392505050565b5f8060408385031215610dec575f80fd5b610df583610ca0565b946020939093013593505050565b60208082526016908201527550726f6475637420616c72656164792065786973747360501b604082015260600190565b60208082526029908201527f5468652063757272656e742070726f64756374206379636c652063616e6e6f7460408201526808189948185919195960ba1b606082015260800190565b634e487b7160e01b5f52603260045260245ffd5b634e487b7160e01b5f52601160045260245ffd5b8082028115828204841417610ebb57610ebb610e90565b92915050565b80820180821115610ebb57610ebb610e90565b5f82610eee57634e487b7160e01b5f52601260045260245ffd5b500690565b5f60208284031215610f03575f80fd5b5051919050565b5f60208284031215610f1a575f80fd5b81518015158114610f29575f80fd5b939250505056fea2646970667358221220fa2626aa7e6dd96832c24c9541cedbb73487819d1d1aea74912377f6e93a994364736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CONFIRMJOIN = "confirmJoin";

    public static final String FUNC_CREATEPRODUCT = "createProduct";

    public static final String FUNC_OPENDRAW = "openDraw";

    public static final String FUNC_PRODUCTS = "products";

    public static final Event CREATEPRODUCT_EVENT = new Event("CreateProduct", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event CREATEPRODUCTCYCLE_EVENT = new Event("CreateProductCycle", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OPENDRAW_EVENT = new Event("OpenDraw", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFEREXECUTED_EVENT = new Event("TransferExecuted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event USERCONFIRMJOIN_EVENT = new Event("UserConfirmJoin", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event WAITDRAW_EVENT = new Event("WaitDraw", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected Main(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Main(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Main(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Main(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<CreateProductEventResponse> getCreateProductEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CREATEPRODUCT_EVENT, transactionReceipt);
        ArrayList<CreateProductEventResponse> responses = new ArrayList<CreateProductEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            CreateProductEventResponse typedResponse = new CreateProductEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.token_address = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.product_value = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.start_index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.face_value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.fee_value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.fee_address = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.ticket_value = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.max_count = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.start_lock_code = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CreateProductEventResponse getCreateProductEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CREATEPRODUCT_EVENT, log);
        CreateProductEventResponse typedResponse = new CreateProductEventResponse();
        typedResponse.log = log;
        typedResponse.token_address = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.product_value = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.start_index = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.face_value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.fee_value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.fee_address = (String) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.ticket_value = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
        typedResponse.max_count = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
        typedResponse.start_lock_code = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
        return typedResponse;
    }

    public Flowable<CreateProductEventResponse> createProductEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCreateProductEventFromLog(log));
    }

    public Flowable<CreateProductEventResponse> createProductEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CREATEPRODUCT_EVENT));
        return createProductEventFlowable(filter);
    }

    public static List<CreateProductCycleEventResponse> getCreateProductCycleEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CREATEPRODUCTCYCLE_EVENT, transactionReceipt);
        ArrayList<CreateProductCycleEventResponse> responses = new ArrayList<CreateProductCycleEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            CreateProductCycleEventResponse typedResponse = new CreateProductCycleEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.token_address = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.product_value = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.current_index = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.state = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.total_value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.win_code = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.win_address = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.win_block_number = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.claim_state = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static CreateProductCycleEventResponse getCreateProductCycleEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CREATEPRODUCTCYCLE_EVENT, log);
        CreateProductCycleEventResponse typedResponse = new CreateProductCycleEventResponse();
        typedResponse.log = log;
        typedResponse.token_address = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.product_value = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.current_index = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.state = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.total_value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.win_code = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.win_address = (String) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.win_block_number = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
        typedResponse.claim_state = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
        return typedResponse;
    }

    public Flowable<CreateProductCycleEventResponse> createProductCycleEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getCreateProductCycleEventFromLog(log));
    }

    public Flowable<CreateProductCycleEventResponse> createProductCycleEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CREATEPRODUCTCYCLE_EVENT));
        return createProductCycleEventFlowable(filter);
    }

    public static List<OpenDrawEventResponse> getOpenDrawEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OPENDRAW_EVENT, transactionReceipt);
        ArrayList<OpenDrawEventResponse> responses = new ArrayList<OpenDrawEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OpenDrawEventResponse typedResponse = new OpenDrawEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.token_address = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.product_value = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.current_index = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.win_code = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.win_address = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.win_block_number = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.state = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.claim_state = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static OpenDrawEventResponse getOpenDrawEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OPENDRAW_EVENT, log);
        OpenDrawEventResponse typedResponse = new OpenDrawEventResponse();
        typedResponse.log = log;
        typedResponse.token_address = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.product_value = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.current_index = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.win_code = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.win_address = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.win_block_number = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.state = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.claim_state = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
        return typedResponse;
    }

    public Flowable<OpenDrawEventResponse> openDrawEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOpenDrawEventFromLog(log));
    }

    public Flowable<OpenDrawEventResponse> openDrawEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OPENDRAW_EVENT));
        return openDrawEventFlowable(filter);
    }

    public static List<TransferExecutedEventResponse> getTransferExecutedEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TRANSFEREXECUTED_EVENT, transactionReceipt);
        ArrayList<TransferExecutedEventResponse> responses = new ArrayList<TransferExecutedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferExecutedEventResponse typedResponse = new TransferExecutedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.token = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.sender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.recipient = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TransferExecutedEventResponse getTransferExecutedEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TRANSFEREXECUTED_EVENT, log);
        TransferExecutedEventResponse typedResponse = new TransferExecutedEventResponse();
        typedResponse.log = log;
        typedResponse.token = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.sender = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.recipient = (String) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<TransferExecutedEventResponse> transferExecutedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTransferExecutedEventFromLog(log));
    }

    public Flowable<TransferExecutedEventResponse> transferExecutedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFEREXECUTED_EVENT));
        return transferExecutedEventFlowable(filter);
    }

    public static List<UserConfirmJoinEventResponse> getUserConfirmJoinEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(USERCONFIRMJOIN_EVENT, transactionReceipt);
        ArrayList<UserConfirmJoinEventResponse> responses = new ArrayList<UserConfirmJoinEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            UserConfirmJoinEventResponse typedResponse = new UserConfirmJoinEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.token_address = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.product_value = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.current_index = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.user_address = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.count = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.block_number = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.lock_code = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.ticket_value = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static UserConfirmJoinEventResponse getUserConfirmJoinEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(USERCONFIRMJOIN_EVENT, log);
        UserConfirmJoinEventResponse typedResponse = new UserConfirmJoinEventResponse();
        typedResponse.log = log;
        typedResponse.token_address = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.product_value = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.current_index = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.user_address = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.count = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.block_number = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.lock_code = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.ticket_value = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
        return typedResponse;
    }

    public Flowable<UserConfirmJoinEventResponse> userConfirmJoinEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getUserConfirmJoinEventFromLog(log));
    }

    public Flowable<UserConfirmJoinEventResponse> userConfirmJoinEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(USERCONFIRMJOIN_EVENT));
        return userConfirmJoinEventFlowable(filter);
    }

    public static List<WaitDrawEventResponse> getWaitDrawEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(WAITDRAW_EVENT, transactionReceipt);
        ArrayList<WaitDrawEventResponse> responses = new ArrayList<WaitDrawEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            WaitDrawEventResponse typedResponse = new WaitDrawEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.token_address = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.product_value = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.current_index = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.block_number = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static WaitDrawEventResponse getWaitDrawEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(WAITDRAW_EVENT, log);
        WaitDrawEventResponse typedResponse = new WaitDrawEventResponse();
        typedResponse.log = log;
        typedResponse.token_address = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.product_value = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.current_index = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.block_number = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<WaitDrawEventResponse> waitDrawEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getWaitDrawEventFromLog(log));
    }

    public Flowable<WaitDrawEventResponse> waitDrawEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(WAITDRAW_EVENT));
        return waitDrawEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> confirmJoin(String token_address,
            BigInteger product_value, BigInteger count) {
        final Function function = new Function(
                FUNC_CONFIRMJOIN, 
                Arrays.<Type>asList(new Address(160, token_address),
                new Uint256(product_value),
                new Uint256(count)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createProduct(String token_address,
            BigInteger product_value, BigInteger face_value, BigInteger fee_value,
            String fee_address, BigInteger ticket_value, BigInteger max_count) {
        final Function function = new Function(
                FUNC_CREATEPRODUCT, 
                Arrays.<Type>asList(new Address(160, token_address),
                new Uint256(product_value),
                new Uint256(face_value),
                new Uint256(fee_value),
                new Address(160, fee_address),
                new Uint256(ticket_value),
                new Uint256(max_count)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> openDraw(String token_address,
            BigInteger product_value, BigInteger current_index, List<BlockVo> vos) {
        final Function function = new Function(
                FUNC_OPENDRAW, 
                Arrays.<Type>asList(new Address(160, token_address),
                new Uint256(product_value),
                new Uint256(current_index),
                new org.web3j.abi.datatypes.DynamicArray<BlockVo>(BlockVo.class, vos)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple9<String, BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger>> products(
            String param0, BigInteger param1) {
        final Function function = new Function(FUNC_PRODUCTS, 
                Arrays.<Type>asList(new Address(160, param0),
                new Uint256(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple9<String, BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple9<String, BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple9<String, BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger> call(
                            ) throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple9<String, BigInteger, BigInteger, BigInteger, BigInteger, String, BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (String) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (BigInteger) results.get(7).getValue(), 
                                (BigInteger) results.get(8).getValue());
                    }
                });
    }

    @Deprecated
    public static Main load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new Main(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Main load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Main(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Main load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new Main(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Main load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Main(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Main> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Main.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<Main> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Main.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Main> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice,
            BigInteger gasLimit) {
        return deployRemoteCall(Main.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Main> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Main.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class BlockVo extends StaticStruct {
        public BigInteger number;

        public BigInteger transaction_count;

        public BigInteger timestamp;

        public BlockVo(BigInteger number, BigInteger transaction_count, BigInteger timestamp) {
            super(new Uint256(number),
                    new Uint256(transaction_count),
                    new Uint256(timestamp));
            this.number = number;
            this.transaction_count = transaction_count;
            this.timestamp = timestamp;
        }

        public BlockVo(Uint256 number, Uint256 transaction_count, Uint256 timestamp) {
            super(number, transaction_count, timestamp);
            this.number = number.getValue();
            this.transaction_count = transaction_count.getValue();
            this.timestamp = timestamp.getValue();
        }
    }

    public static class CreateProductEventResponse extends BaseEventResponse {
        public String token_address;

        public BigInteger product_value;

        public BigInteger start_index;

        public BigInteger face_value;

        public BigInteger fee_value;

        public String fee_address;

        public BigInteger ticket_value;

        public BigInteger max_count;

        public BigInteger start_lock_code;
    }

    public static class CreateProductCycleEventResponse extends BaseEventResponse {
        public String token_address;

        public BigInteger product_value;

        public BigInteger current_index;

        public BigInteger state;

        public BigInteger total_value;

        public BigInteger win_code;

        public String win_address;

        public BigInteger win_block_number;

        public BigInteger claim_state;
    }

    public static class OpenDrawEventResponse extends BaseEventResponse {
        public String token_address;

        public BigInteger product_value;

        public BigInteger current_index;

        public BigInteger win_code;

        public String win_address;

        public BigInteger win_block_number;

        public BigInteger state;

        public BigInteger claim_state;
    }

    public static class TransferExecutedEventResponse extends BaseEventResponse {
        public String token;

        public String sender;

        public String recipient;

        public BigInteger amount;
    }

    public static class UserConfirmJoinEventResponse extends BaseEventResponse {
        public String token_address;

        public BigInteger product_value;

        public BigInteger current_index;

        public String user_address;

        public BigInteger count;

        public BigInteger block_number;

        public BigInteger lock_code;

        public BigInteger ticket_value;
    }

    public static class WaitDrawEventResponse extends BaseEventResponse {
        public String token_address;

        public BigInteger product_value;

        public BigInteger current_index;

        public BigInteger block_number;
    }
}
