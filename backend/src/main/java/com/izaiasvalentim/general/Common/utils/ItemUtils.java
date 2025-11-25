package com.izaiasvalentim.general.Common.utils;

import com.izaiasvalentim.general.Domain.Item;
import com.izaiasvalentim.general.Domain.Produto;

public class ItemUtils {


    public static void generateItemBatch(Produto prod, Item item){
        if (prod.getName() == null || prod.getName().length() < 2) {
            throw new IllegalArgumentException("O nome do item deve ter pelo menos 2 caracteres.");
        }
        String prefixo = prod.getName().substring(0, 2).toUpperCase();
        item.setBatch(prefixo + item.getId());
    }
}
