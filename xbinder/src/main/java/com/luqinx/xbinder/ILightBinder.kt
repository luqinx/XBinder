package com.luqinx.xbinder
/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
interface ILightBinder: IProxyBehaviors {
    /**
     * override this method for listening the communication process death
     */
    @JvmDefault fun onBinderDeath(process: String) { }

    /**
     * override this method for listening the communication process restarted
     */
    @JvmDefault fun onProcessRestarted(process: String) { }

    /**
     * override this method for listening the communication process ready
     *
     * @see XBinder.notifyProcessReady
     */
    @JvmDefault fun onProcessReady(process: String) { }
}
