package com.luqinx.xbinder

/**
 *
 *  !!! don't override the methods in this class
 *  it's will be intercepted by proxy

 *
 * @author  qinchao
 *
 * @since 2022/1/2
 */
interface IProxyBehaviors {
    /**
     *  !!! don't override this method
     *  it's will be intercepted by proxy
     *
     *  create remote service
     */
    @JvmDefault fun `_$newConstructor_`(consTypes: Array<*>?, consArgs: Array<*>?): String? = null

    /**
     *  !!! don't override this method
     *  it's will be intercepted by proxy
     *
     *  @return return true if origin process is dead
     */
    @JvmDefault fun `_$bindCallbackProxy_`(instanceId: String): Boolean = false

    /**
     *  !!! don't override this method
     *  it's will be intercepted by proxy
     *
     *  @return return true if origin process is dead
     */
    @JvmDefault fun isBinderAlive(): Boolean = false// will be intercepted by ServiceLocalBehaviors

    /**
     *  !!! don't override this method
     *  it's will be intercepted by proxy
     *
     *  @return return true if origin process is exist
     */
    @JvmDefault fun isRemoteServiceExist(): Boolean = false // will be intercepted by ServiceLocalBehaviors

    /**
     *  !!! don't override this method
     *  it's will be intercepted by proxy
     *
     *  @return return the name of the process who communication with
     */
    @JvmDefault fun getRemoteProcessName(): String = "" // will be intercepted by ServiceLocalBehaviors

    @JvmDefault fun getInstanceId(): String? = null

    @JvmDefault fun setInstanceId(instanceId: String?) {}
}