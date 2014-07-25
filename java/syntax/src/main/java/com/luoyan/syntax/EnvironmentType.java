package com.luoyan.syntax;

public enum EnvironmentType {

    /** One box */
    ONEBOX(false),

    /** Staging server */
    STAGING(false),

    /** Production */
    PRODUCTION(true),

    /** Shangdi Production */
    SHANGDI(true),

    /** Lugu Production */
    LUGU(true),

    /** Guigu Production */
    GUIGU(true),

    /** Haihang Production */
    HAIHANG(true),

    /** Stress environment */
    STRESS(false);

    private boolean production;

    EnvironmentType(boolean production) {
        this.production = production;
    }

    public boolean isProduction() {
        return this.production;
    }
}