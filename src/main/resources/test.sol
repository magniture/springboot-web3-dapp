// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract Test {
    uint256 value;

    function setValue(uint256 _value) public {
        value = _value;
    }

    function getValue() public view returns (uint256) {
        return value;
    }

    function getName() public view returns (uint256) {
        return value;
    }
}
