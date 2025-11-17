package com.izaiasvalentim.general.Common.utils;

import com.izaiasvalentim.general.Domain.Item;

public class ItemUtils {


    public static void generateItemBatch(Item item){
        if (item.getName() == null || item.getName().length() < 2) {
            throw new IllegalArgumentException("O nome do item deve ter pelo menos 2 caracteres.");
        }
        String prefixo = item.getName().substring(0, 2).toUpperCase();
        item.setBatch(prefixo + item.getId());
    }
}
