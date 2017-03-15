(function(cvsId) {
    // The item being dragged, if any, and offset of last mouse down
    var dragging = null;
    var offset = null;

    // Get mouse position as an object
    function getMousePos(cvs, evt) {
        var rect = cvs.getBoundingClientRect();
        return {
            x: evt.clientX - rect.left,
            y: evt.clientY - rect.top
        };
    }

    // All shapes beng managed
    var shapes = [
        new Box(150,  50, 17, 17, 'gray'),
        new Box(150, 150, 17, 17, 'gray'),
        new Box(100, 200, 17, 17, 'red'),   // Control point 1
        new Box(100, 100, 17, 17, 'green'), // Control point 2
        new Box( 50, 150, 17, 17, 'gray'),
        new Box( 50,  50, 17, 17, 'gray')
    ];

    // Control Box object
    function Box(x, y, w, h, clr) {
        // Location and size
        this.x = x; // Center point
        this.y = y;
        this.w = w; // Dimensions
        this.h = h;
        this.clr = clr || 'black';

        // Half width and half height parameters
        var hw = 0.5*w;
        var hh = 0.5*h;

        // Draw the box
        this.draw = function(ctx) {
            ctx.strokeStyle = this.clr;
            ctx.strokeRect(this.x-hw,this.y-hh,this.w,this.h);
        }

        // Test if point hit the box. If so, return an offset object.
        this.hit = function(x, y) {
            if (x > (this.x - hw) && x < (this.x + hw) && y > (this.y - hh) && y < (this.y + hh)) {
                return {dx: x-this.x, dy: y-this.y};
            } else {
                return null;
            }
        }

        // Move the box to the new point and redraw
        this.moveTo = function(x, y) {
            this.x = x;
            this.y = y;
        }
    }

    // Draw all shapes and notations
    function draw(ctx) {
        // Get points
        var pt0 = shapes[0];
        var pt1 = shapes[1];
        var pt2 = shapes[2];    // Control point
        var pt3 = shapes[3];    // Control point
        var pt4 = shapes[4];
        var pt5 = shapes[5];

        // Clear canvas
        ctx.clearRect(0, 0, cvs.width, cvs.height);

        // Draw lines to control points
        ctx.beginPath();
        ctx.moveTo(pt1.x, pt1.y);
        ctx.lineTo(pt2.x, pt2.y);
        ctx.moveTo(pt4.x, pt4.y);
        ctx.lineTo(pt3.x, pt3.y);
        ctx.setLineDash([5, 10]);
        ctx.strokeStyle = 'gray';
        ctx.stroke();
        ctx.setLineDash([]);

        // Draw control boxes
        shapes.forEach( function(item) { item.draw(ctx); } );

        // Draw bezier spline curve
        ctx.beginPath();
        ctx.moveTo(pt0.x, pt0.y);
        ctx.lineTo(pt1.x, pt1.y);
        ctx.bezierCurveTo(pt2.x, pt2.y, pt3.x, pt3.y, pt4.x, pt4.y);
        ctx.lineTo(pt5.x, pt5.y);
        ctx.closePath();
        ctx.strokeStyle = 'black';
        ctx.stroke();

        // Draw control point coordinates
        ctx.font = "12px verdana";
        ctx.fillText("Control point 1: (" + pt2.x + "," + pt2.y + ")", 25, 225);
        ctx.fillText("Control point 2: (" + pt3.x + "," + pt3.y + ")", 25, 240);
    }

    var cvs = document.getElementById(cvsId);
    var ctx = cvs.getContext('2d');

    // Events
    cvs.addEventListener('mousedown', function(evt) {
        var pos = getMousePos(cvs, evt);

        for (var i=0; i<shapes.length; ++i) {
            var shp = shapes[i];
            offset = shp.hit(pos.x, pos.y);
            if (offset) {
                dragging = shp;
                break;
            }
        }
    });

    cvs.addEventListener('mouseup', function(evt) {
        dragging = null;
    });

    cvs.addEventListener('mousemove', function(evt) {
        // Do nothing if nothing is being dragged
        if (!dragging) { return };

        var pos = getMousePos(cvs, evt);
        dragging.moveTo(pos.x-offset.dx, pos.y-offset.dy);
        draw(ctx);
    });

    draw(ctx);
})('cvs2');