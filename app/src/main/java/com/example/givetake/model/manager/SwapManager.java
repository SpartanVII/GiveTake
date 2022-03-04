package com.example.givetake.model.manager;

import com.example.givetake.model.Swap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwapManager {
    Map<String, List<Swap>> swapMap = new HashMap<>();

    public SwapManager(Map<String, List<Swap>> swapMap) {
        this.swapMap = swapMap;
    }


    public List<Swap> getSwaps(String nickName){
        return swapMap.get(nickName);
    }
}
