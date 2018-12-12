// Event : ON_TICK
// client -> client object
// input -> input class
// MathUtils -> 2D math class
load("nashorn:mozilla_compat.js");
importPackage("fr.proneus.engine.graphic");

var MathUtils = MathHelper.static;

// Movement
// TODO client settings
var h = (input.isKeyDown(65) ? -1 : 0) + (input.isKeyDown(68) ? 1 : 0);
var v = (input.isKeyDown(87) ? -1 : 0) + (input.isKeyDown(83) ? 1 : 0);

if(v != 0 || h != 0){
    callServerEvent("move", [h, v]);
}

// Rotate ship to mouse position
var ship = objectManager.getObjectByIdentifier("ship_" + client.getUsername());
if(ship != null){
    var mousePosition = input.getMousePosition();
    var currentAngle = Math.floor(ship.getAngle()+90);
    var newAngle = Math.floor(MathUtils.getAngle(ship.getX(), ship.getY(), mousePosition.getX(), mousePosition.getY()));
    if(currentAngle != newAngle){
        // GameObject functions are only client-side, and ignored when server send object transformation
        // ship.setAngle(newAngle);

        callServerEvent("ship_angle", [newAngle-90]);
    }
}

// Shoot
if(input.isKeyJustDown(81)){
    callServerEvent("shoot");
    log("DEBUG: SHOOT")
}