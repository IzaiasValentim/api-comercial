package com.izaiasvalentim.general.Domain.Enums;

import com.izaiasvalentim.general.Common.CustomExceptions.InvalidRoleException;

public enum TypeRoles {
    ADMIN(1, 100, "ADMINISTRATOR"), // Considere como Administrador do sistema.
    MANAGER(2, 80, "MANAGER"), // Considere Gerente.
    SELLER(3, 60, "SELLER"), // Considere Caixa.
    INTERN(4, 40, "INTERN"); // Considere Serviços internos.

    private final int id;
    private final int level;
    private final String name;

    TypeRoles(int id, int level, String name) {
        this.id = id;
        this.level = level;
        this.name = name;
    }

    public static int getLevelById(int id) {
        for (TypeRoles role : TypeRoles.values()) {
            if (role.id == id) {
                return role.getLevel();
            }
        }
        throw new InvalidRoleException("Invalid role id: " + id);
    }

    public static String getNameById(int id) {
        for (TypeRoles role : TypeRoles.values()) {
            if (role.id == id) {
                return role.getName();
            }
        }
        throw new InvalidRoleException("Invalid role id: " + id);
    }

    public static TypeRoles getTypeById(int id) {
        for (TypeRoles role : TypeRoles.values()) {
            if (role.id == id) {
                return role;
            }
        }
        throw new InvalidRoleException("Invalid role id: " + id);
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
