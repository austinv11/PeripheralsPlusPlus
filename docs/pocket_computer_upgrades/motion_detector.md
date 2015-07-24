# Motion Detector
-----------------

The motion detector upgrade is one which allows a pocket computer to detect... motion. It is crafted with a pocket computer and motion detector.

## Events
|Event|Parameter 1|Parameter 2|Parameter 3|Parameter 4|Description|
|-----|-----------|-----------|-----------|-----------|-----------|
|worldChanged|"worldChanged"|_number_ oldDimId|_number_ newDimId||This event is fired when the computer's world is changed|
|locationChanged|"locationChanged"|_number_ deltaX|_number_ deltaY|_number_ deltaZ|This event is fired when the computer's location is changed|
|rotationChanged|"rotationChanged"|_number_ deltaYaw|_number_ deltaPitch||This event is fired when the player's rotation is changed|
|blockHit|"blockHit"||||This event is fired when the computer is hit against a block|
|rightClick|"rightClick"||||This event is fired when the computer is right clicked upon|
