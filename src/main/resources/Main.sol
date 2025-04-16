// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

// 引入 IERC20 接口
interface IERC20 {
    function transferFrom(
        address sender,
        address recipient,
        uint256 amount
    ) external returns (bool);

    function allowance(address owner, address spender)
    external
    view
    returns (uint256);

    function transfer(address recipient, uint256 amount)
    external
    returns (bool);

    function balanceOf(address account) external view returns (uint256);
}

contract Main {
    struct Order {
        // 金额数据
        uint256 value;
        // 分配的号码
        uint256 lock_code;
        // 参与者地址
        address user_address;
    }

    // 产品周期数据
    struct ProductCycle {
        // 产品周期状态。 10; 参与中，  20；等待开奖 。， 30；已经开奖
        uint256 state;
        // 当前投注总金额
        uint256 total_value;
        // 中奖号码
        uint256 win_code;
        // 中奖地址
        address win_address;
        // 中奖的区块
        uint256 win_block_number;
        // 奖励领取状态 10; 初始化，  20；等待手动领取 。， 30；已经领取
        uint256 claim_state;
        // 订单列表
        Order[] orders;
    }
    // 产品信息
    struct Product {
        // erc20 token地址 100 bnb  1   1bnb 0.1
        address token_address;
        // 开始期号
        uint256 start_index;
        // 当前活跃的期号
        uint256 current_index;
        // 产品面值
        uint256 face_value;
        // 手续费金额
        uint256 fee_value;
        // 受益人地址
        address fee_address;
        // 每参与一次，需要支付的金额
        uint256 ticket_value;
        // 最大参与人数
        uint256 max_count;
        // 幸运号码的开始位置
        uint256 start_lock_code;
        // 产品周期数据 [期号 -> 每期数据]
        mapping(uint256 => ProductCycle) cycles;
    }
    // 区块数据
    struct BlockVo {
        uint256 number;
        uint256 transaction_count;
        uint256 timestamp;
    }

    // [erc20地址 ->  [产品面值 1bnb  10bnb -> 产品信息 ]]
    mapping(address => mapping(uint256 => Product)) public products;

    // 合约所有者
    address admin;

    // 创建产品事件
    event CreateProduct(
    // erc20 token地址 100 bnb  1   1bnb 0.1
        address indexed token_address,
    // 产品面值
        uint256 indexed product_value,
    // 开始期号
        uint256 start_index,
    // 产品面值
        uint256 face_value,
    // 手续费金额
        uint256 fee_value,
    // 受益人地址
        address fee_address,
    // 每参与一次，需要支付的金额
        uint256 ticket_value,
    // 最大参与人数
        uint256 max_count,
    // 幸运号码的开始位置
        uint256 start_lock_code
    );
    // 创建产品周期事件
    event CreateProductCycle(
    // erc20 token地址 100 bnb  1   1bnb 0.1
        address indexed token_address,
    // 产品面值
        uint256 indexed product_value,
    // 当前期号
        uint256 indexed current_index,
    // 产品周期状态。 10; 参与中，  20；等待开奖 。， 30；已经开奖
        uint256 state,
    // 当前投注总金额
        uint256 total_value,
    // 中奖号码
        uint256 win_code,
    // 中奖地址
        address win_address,
    // 中奖的区块
        uint256 win_block_number,
    // 奖励领取状态 10; 初始化，  20；等待手动领取 。， 30；已经领取
        uint256 claim_state
    );
    // 用户参与事件
    event UserConfirmJoin(
    // erc20 token地址 100 bnb  1   1bnb 0.1
        address indexed token_address,
    // 产品面值
        uint256 indexed product_value,
    // 当前期号
        uint256 indexed current_index,
    // 调用人
        address user_address,
    // 参加次数
        uint256 count,
    // 在那个区块调用的
        uint256 block_number,
    // 分配的幸运号码
        uint256 lock_code,
    // 产品价值
        uint256 ticket_value
    );

    // 事件：成功转账时触发
    event TransferExecuted(
        address indexed token,
        address indexed sender,
        address indexed recipient,
        uint256 amount
    );

    // 等待开奖
    event WaitDraw(
    // erc20 token地址 100 bnb  1   1bnb 0.1
        address indexed token_address,
    // 产品面值
        uint256 indexed product_value,
    // 当前期号
        uint256 indexed current_index,
    // 在那个区块调用的
        uint256 block_number
    );

    event OpenDraw(
    // erc20 token地址 100 bnb  1   1bnb 0.1
        address indexed token_address,
    // 产品面值
        uint256 indexed product_value,
    // 当前期号
        uint256 indexed current_index,
    // 中奖号码
        uint256 win_code,
    // 中奖地址
        address win_address,
    // 中奖的区块
        uint256 win_block_number,

    // 产品周期状态。 10; 参与中，  20；等待开奖 。， 30；已经开奖
        uint256 state,

    // 奖励领取状态 10; 初始化，  20；等待手动领取 。， 30；已经领取
        uint256 claim_state
    );

    constructor() {
        admin = msg.sender;
    }

    modifier checkAdmin() {
        require(msg.sender == admin, "is not admin");
        _;
    }

    // 创建产品方法
    function createProduct(
        address token_address,
        uint256 product_value,
        uint256 face_value,
        uint256 fee_value,
        address fee_address,
        uint256 ticket_value,
        uint256 max_count
    ) public checkAdmin {
        require(
            products[token_address][product_value].token_address == address(0),
            "Product already exists"
        );

        Product storage product = products[token_address][product_value];
        product.token_address = token_address;
        product.start_index = 100;
        product.face_value = face_value;
        product.fee_value = fee_value;
        product.fee_address = fee_address;
        product.ticket_value = ticket_value;
        product.max_count = max_count;
        product.start_lock_code = 10000001;

        // 触发创建产品事件，通知后台也保存
        emit CreateProduct(
            token_address,
            product_value,
            product.start_index,
            face_value,
            fee_value,
            fee_address,
            ticket_value,
            max_count,
            product.start_lock_code
        );

        // 默认为初始化第一期
        uint256 current_index = product.start_index + 1;
        _next_cycle(token_address, product_value, current_index, product);

        // product.current_index = current_index;
        // ProductCycle storage cycle = product.cycles[current_index];
        // cycle.state = 10;
        // cycle.claim_state = 10;

        // // 触发周期创建事件
        // emit CreateProductCycle(
        //     token_address,
        //     product_value,
        //     current_index,
        //     cycle.state,
        //     cycle.total_value,
        //     cycle.win_code,
        //     cycle.win_address,
        //     cycle.win_block_number,
        //     cycle.claim_state
        // );
    }

    // 玩家参与
    function confirmJoin(
        address token_address,
        uint256 product_value,
        uint256 count
    ) public {
        require(
            products[token_address][product_value].token_address != address(0),
            "Product already exists"
        );
        Product storage product = products[token_address][product_value];

        // 获取当前活动的期号
        uint256 current_index = product.current_index;
        ProductCycle storage cycle = product.cycles[current_index];
        //检查产品周期状态。 10; 参与中，  20；等待开奖 。， 30；已经开奖
        require(cycle.state == 10, "The current product cycle cannot be added");

        //检查订单已经达到上限 0 + 12 <= 10
        require(
            cycle.orders.length + count <= product.max_count,
            "Maximum number of participation"
        );

        // 效验金额
        uint256 amount = count * product.ticket_value;
        transferTokens(
            product.token_address,
            msg.sender,
            address(this),
            amount
        );

        // 保存订单 - 生成幸运号码
        for (uint256 i = 0; i < count; i++) {
            //10000001 + 0 = 10000001
            //10000001 + 1 = 10000002
            //10000001 + 2 = 10000003
            //...
            uint256 lock_code = product.start_lock_code + cycle.orders.length;
            // 保存订单
            cycle.orders.push(
                Order({
                    value: product.ticket_value,
                    lock_code: lock_code,
                    user_address: msg.sender
                })
            );
            // 触发事件
            emit UserConfirmJoin(
                token_address,
                product_value,
                current_index,
                msg.sender,
                count,
                block.number,
                lock_code,
                product.ticket_value
            );
        }
        // 保存当前投资总金额吗
        cycle.total_value = cycle.total_value + amount;

        // cycle.orders
        // 订单是否满员
        if (cycle.orders.length == product.max_count) {
            // 1、如果已经满员，触发事件，等待开奖
            emit WaitDraw(
                token_address,
                product_value,
                current_index,
                block.number
            );
            // 10; 参与中，  20；等待开奖 。， 30；已经开奖
            cycle.state = 20;

            // 2、初始化下一期，开启新的一轮
            _next_cycle(
                token_address,
                product_value,
                current_index + 1,
                product
            );
        }
    }

    // 初始化下一期
    function _next_cycle(
        address token_address,
        uint256 product_value,
        uint256 current_index,
        Product storage product
    ) internal {
        // 默认为初始化第一期
        // uint256 current_index = product.start_index + 1;
        product.current_index = current_index;
        ProductCycle storage cycle = product.cycles[current_index];
        cycle.state = 10;
        cycle.claim_state = 10;

        // 触发周期创建事件
        emit CreateProductCycle(
            token_address,
            product_value,
            current_index,
            cycle.state,
            cycle.total_value,
            cycle.win_code,
            cycle.win_address,
            cycle.win_block_number,
            cycle.claim_state
        );
    }

    /**
     * @notice 从用户账户转账指定数量的 ERC20 代币到目标地址
     * @dev 用户必须先通过 ERC20 `approve` 方法授权本合约可以使用指定的代币数量
     * @param token ERC-20 代币合约地址
     * @param sender 代币持有者地址
     * @param recipient 代币接收者地址
     * @param amount 转账数量
     */
    function transferTokens(
        address token,
        address sender,
        address recipient,
        uint256 amount
    ) internal {
        IERC20 erc20 = IERC20(token);

        // 1. 检查授权额度是否足够
        uint256 approvedAmount = erc20.allowance(sender, address(this));
        require(approvedAmount >= amount, "Insufficient allowance");

        // 2. 调用 transferFrom 从 `sender` 转移 `amount` 到 `recipient`
        bool success = erc20.transferFrom(sender, recipient, amount);
        require(success, "Transfer failed");

        // 3. 触发转账事件
        emit TransferExecuted(token, sender, recipient, amount);
    }

    /**
     * @notice 合约直接发送 ERC-20 代币
     * @dev 需要合约本身有足够的代币余额
     * @param token ERC-20 代币合约地址
     * @param recipient 代币接收者地址
     * @param amount 转账数量
     */
    function sendTokens(
        address token,
        address recipient,
        uint256 amount
    ) internal {
        IERC20 erc20 = IERC20(token);

        // 1. 确保合约有足够的代币
        uint256 contractBalance = erc20.balanceOf(address(this));
        require(contractBalance >= amount, "Insufficient contract balance");

        // 2. 直接转账
        bool success = erc20.transfer(recipient, amount);
        require(success, "Transfer failed");

        // 3. 触发转账事件
        emit TransferExecuted(token, address(this), recipient, amount);
    }

    function openDraw(
        address token_address,
        uint256 product_value,
        uint256 current_index,
        BlockVo[] calldata vos
    ) public checkAdmin {
        require(
            products[token_address][product_value].token_address != address(0),
            "Product already exists"
        );

        Product storage product = products[token_address][product_value];

        // 获取当前活动的期号
        // uint256 current_index = product.current_index;
        ProductCycle storage cycle = product.cycles[current_index];
        //检查产品周期状态。 10; 参与中，  20；等待开奖 。， 30；已经开奖
        require(cycle.state == 20, "The current product cycle cannot be added");

        // 区块产生的随机数
        uint256 total = 0;
        for (uint256 i = 0; i < vos.length; i++) {
            BlockVo calldata vo = vos[i];
            uint256 a = vo.number * vo.transaction_count;
            total = total + a + vo.timestamp;
        }

        uint256 b = total % product.max_count;
        // 中奖号码
        uint256 lock_code = product.start_lock_code + b;

        // 找到幸运玩家
        // address win_address;
        for (uint256 i = 0; i < cycle.orders.length; i++) {
            Order memory order = cycle.orders[i];
            if (order.lock_code == lock_code) {
                // 代表中奖了
                cycle.win_address = order.user_address;
                cycle.win_code = lock_code;
                cycle.win_block_number = block.number;
                break;
            }
        }

        if (product.fee_value > 0) {
            // 给受益人转账
            sendTokens(token_address, product.fee_address, product.fee_value);
        }

        // 给中奖者转账
        sendTokens(token_address, cycle.win_address, product.face_value);

        cycle.state = 30;
        cycle.claim_state = 30;

        // 触发开奖事件
        emit OpenDraw(
            token_address,
            product_value,
            current_index,
            cycle.win_code,
            cycle.win_address,
            cycle.win_block_number,
            cycle.state,
            cycle.claim_state
        );
    }
}
