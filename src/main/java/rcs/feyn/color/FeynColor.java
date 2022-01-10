package rcs.feyn.color;

import rcs.feyn.math.MathUtils;
import rcs.feyn.utils.XORShift;

public final class FeynColor {
  
  public static final FeynColor snow                 = new FeynColor(255, 250, 250);
  public static final FeynColor snow2                = new FeynColor(238, 233, 233);
  public static final FeynColor snow3                = new FeynColor(205, 201, 201);
  public static final FeynColor snow4                = new FeynColor(139, 137, 137);
  public static final FeynColor ghostWhite           = new FeynColor(248, 248, 255);
  public static final FeynColor whiteSmoke           = new FeynColor(245, 245, 245);
  public static final FeynColor gainsboro            = new FeynColor(220, 220, 220);
  public static final FeynColor floralWhite          = new FeynColor(255, 250, 240);
  public static final FeynColor oldLace              = new FeynColor(253, 245, 230);
  public static final FeynColor linen                = new FeynColor(240, 240, 230);
  public static final FeynColor antiqueWhite         = new FeynColor(250, 235, 215);
  public static final FeynColor antiqueWhite2        = new FeynColor(238, 223, 204);
  public static final FeynColor antiqueWhite3        = new FeynColor(205, 192, 176);
  public static final FeynColor antiqueWhite4        = new FeynColor(139, 131, 120);
  public static final FeynColor papayaWhip           = new FeynColor(255, 239, 213);
  public static final FeynColor blanchedAlmond       = new FeynColor(255, 235, 205);
  public static final FeynColor bisque               = new FeynColor(255, 228, 196);
  public static final FeynColor bisque2              = new FeynColor(238, 213, 183);
  public static final FeynColor bisque3              = new FeynColor(205, 183, 158);
  public static final FeynColor bisque4              = new FeynColor(139, 125, 107);
  public static final FeynColor peachPuff            = new FeynColor(255, 218, 185);
  public static final FeynColor peachPuff2           = new FeynColor(238, 203, 173);
  public static final FeynColor peachPuff3           = new FeynColor(205, 175, 149);
  public static final FeynColor peachPuff4           = new FeynColor(139, 119, 101);
  public static final FeynColor navajoWhite          = new FeynColor(255, 222, 173);
  public static final FeynColor moccasin             = new FeynColor(255, 228, 181);
  public static final FeynColor cornsilk             = new FeynColor(255, 248, 220);
  public static final FeynColor cornsilk2            = new FeynColor(238, 232, 205);
  public static final FeynColor cornsilk3            = new FeynColor(205, 200, 177);
  public static final FeynColor cornsilk4            = new FeynColor(139, 136, 120);
  public static final FeynColor ivory                = new FeynColor(255, 255, 240);
  public static final FeynColor ivory2               = new FeynColor(238, 238, 224);
  public static final FeynColor ivory3               = new FeynColor(205, 205, 193);
  public static final FeynColor ivory4               = new FeynColor(139, 139, 131);
  public static final FeynColor lemonChiffon         = new FeynColor(255, 250, 205);
  public static final FeynColor seashell             = new FeynColor(255, 245, 238);
  public static final FeynColor seashell2            = new FeynColor(238, 229, 222);
  public static final FeynColor seashell3            = new FeynColor(205, 197, 191);
  public static final FeynColor seashell4            = new FeynColor(139, 134, 130);
  public static final FeynColor honeydew             = new FeynColor(240, 255, 240);
  public static final FeynColor honeydew2            = new FeynColor(244, 238, 224);
  public static final FeynColor honeydew3            = new FeynColor(193, 205, 193);
  public static final FeynColor honeydew4            = new FeynColor(131, 139, 131);
  public static final FeynColor mintCream            = new FeynColor(245, 255, 250);
  public static final FeynColor azure                = new FeynColor(240, 255, 255);
  public static final FeynColor aliceBlue            = new FeynColor(240, 248, 255);
  public static final FeynColor lavender             = new FeynColor(230, 230, 250);
  public static final FeynColor lavenderBlush        = new FeynColor(255, 240, 245);
  public static final FeynColor mistyRose            = new FeynColor(255, 228, 225);
  public static final FeynColor white                = new FeynColor(255, 255, 255);
  public static final FeynColor black                = new FeynColor(  0,   0,   0);
  public static final FeynColor darkGray             = new FeynColor( 50,  50,  50);
  public static final FeynColor veryDarkGray         = new FeynColor( 20,  20,  20);
  public static final FeynColor superDarkGray        = new FeynColor( 10,  10,  10);
  public static final FeynColor darkSlateGray        = new FeynColor( 49,  79,  79);
  public static final FeynColor dimGray              = new FeynColor(105, 105, 105);
  public static final FeynColor slateGray            = new FeynColor(112, 138, 144);
  public static final FeynColor lightSlateGray       = new FeynColor(119, 136, 153);
  public static final FeynColor gray                 = new FeynColor(190, 190, 190);
  public static final FeynColor lightGray            = new FeynColor(211, 211, 211);
  public static final FeynColor midnightBlue         = new FeynColor( 25,  25, 112);
  public static final FeynColor navy                 = new FeynColor(  0,   0, 128);
  public static final FeynColor cornflowerBlue       = new FeynColor(100, 149, 237);
  public static final FeynColor darkSlateBlue        = new FeynColor( 72,  61, 139);
  public static final FeynColor slateBlue            = new FeynColor(106,  90, 205);
  public static final FeynColor mediumSlateBlue      = new FeynColor(123, 104, 238);
  public static final FeynColor lightSlateBlue       = new FeynColor(132, 112, 255);
  public static final FeynColor mediumBlue           = new FeynColor(  0,   0, 205);
  public static final FeynColor royalBlue            = new FeynColor( 65, 105, 225);
  public static final FeynColor blue                 = new FeynColor(  0,   0, 255);
  public static final FeynColor dodgerBlue           = new FeynColor( 30, 144, 255);
  public static final FeynColor deepSkyBlue          = new FeynColor(  0, 191, 255);
  public static final FeynColor skyBlue              = new FeynColor(135, 206, 250);
  public static final FeynColor lightSkyBlue         = new FeynColor(135, 206, 250);
  public static final FeynColor steelBlue            = new FeynColor( 70, 130, 180);
  public static final FeynColor lightSteelBlue       = new FeynColor(176, 196, 222);
  public static final FeynColor lightBlue            = new FeynColor(173, 216, 230);
  public static final FeynColor powderBlue           = new FeynColor(176, 224, 230);
  public static final FeynColor paleTurquoise        = new FeynColor(175, 238, 238);
  public static final FeynColor darkTurquoise        = new FeynColor(  0, 206, 209);
  public static final FeynColor mediumTurquoise      = new FeynColor( 72, 209, 204);
  public static final FeynColor turquoise            = new FeynColor( 64, 224, 208);
  public static final FeynColor cyan                 = new FeynColor(  0, 255, 255);
  public static final FeynColor lightCyan            = new FeynColor(224, 255, 255);
  public static final FeynColor cadetBlue            = new FeynColor( 95, 158, 160);
  public static final FeynColor mediumAquamarine     = new FeynColor(102, 205, 170);
  public static final FeynColor aquamarine           = new FeynColor(127, 255, 212);
  public static final FeynColor green                = new FeynColor(  0, 255,   0);
  public static final FeynColor darkGreen            = new FeynColor(  0, 100,   0);
  public static final FeynColor darkOliveGreen       = new FeynColor( 85, 107,  47);
  public static final FeynColor darkSeaGreen         = new FeynColor(143, 188, 143);
  public static final FeynColor seaGreen             = new FeynColor( 46, 139,  87);
  public static final FeynColor mediumSeaGreen       = new FeynColor( 60, 179, 113);
  public static final FeynColor lightSeaGreen        = new FeynColor( 32, 178, 170);
  public static final FeynColor paleGreen            = new FeynColor(152, 251, 152);
  public static final FeynColor springGreen          = new FeynColor(  0, 255, 127);
  public static final FeynColor lawnGreen            = new FeynColor(124, 252,   0);
  public static final FeynColor chartreuse           = new FeynColor(127, 255,   0);
  public static final FeynColor mediumSpringGreen    = new FeynColor(  0, 250, 154);
  public static final FeynColor greenYellow          = new FeynColor(173, 255,  47);
  public static final FeynColor limeGreen            = new FeynColor( 50, 205,  50);
  public static final FeynColor yellowGreen          = new FeynColor(154, 205,  50);
  public static final FeynColor forestGreen          = new FeynColor( 34, 139,  34);
  public static final FeynColor oliveDrab            = new FeynColor(107, 142,  35);
  public static final FeynColor darkKhaki            = new FeynColor(189, 183, 107);
  public static final FeynColor khaki                = new FeynColor(240, 230, 140);
  public static final FeynColor paleGoldenrod        = new FeynColor(238, 232, 170);
  public static final FeynColor lightGoldenrodYellow = new FeynColor(250, 250, 210);
  public static final FeynColor lightYellow          = new FeynColor(255, 255, 224);
  public static final FeynColor yellow               = new FeynColor(255, 255,   0);
  public static final FeynColor gold                 = new FeynColor(255, 215,   0);
  public static final FeynColor lightGoldenrod       = new FeynColor(238, 221, 130);
  public static final FeynColor goldenrod            = new FeynColor(218, 165,  32);
  public static final FeynColor darkGoldenrod        = new FeynColor(184, 134,  11);
  public static final FeynColor rosyBrown            = new FeynColor(188, 143, 143);
  public static final FeynColor indianRed            = new FeynColor(205,  92,  92);
  public static final FeynColor saddleBrown          = new FeynColor(139,  69,  19);
  public static final FeynColor sienna               = new FeynColor(160,  82,  45);
  public static final FeynColor peru                 = new FeynColor(205, 133,  63);
  public static final FeynColor burlywood            = new FeynColor(222, 184, 135);
  public static final FeynColor beige                = new FeynColor(245, 245, 220);
  public static final FeynColor wheat                = new FeynColor(245, 222, 179);
  public static final FeynColor sandyBrown           = new FeynColor(244, 164,  96);
  public static final FeynColor tan                  = new FeynColor(210, 180, 140);
  public static final FeynColor chocolate            = new FeynColor(210, 105,  30);
  public static final FeynColor firebrick            = new FeynColor(178,  34,  34);
  public static final FeynColor brown                = new FeynColor(165,  42,  42);
  public static final FeynColor darkSalmon           = new FeynColor(233, 150, 122);
  public static final FeynColor salmon               = new FeynColor(250, 128, 114);
  public static final FeynColor lightSalmon          = new FeynColor(255, 160, 122);
  public static final FeynColor orange               = new FeynColor(255, 165,   0);
  public static final FeynColor darkOrange           = new FeynColor(255, 140,   0);
  public static final FeynColor coral                = new FeynColor(255, 127,  80);
  public static final FeynColor lightCoral           = new FeynColor(240, 128, 128);
  public static final FeynColor tomato               = new FeynColor(255,  99,  71);
  public static final FeynColor orangeRed            = new FeynColor(255,  69,   0);
  public static final FeynColor red                  = new FeynColor(255,   0,   0);
  public static final FeynColor hotPink              = new FeynColor(255, 105, 180);
  public static final FeynColor deepPink             = new FeynColor(255,  20, 147);
  public static final FeynColor pink                 = new FeynColor(255, 192, 203);
  public static final FeynColor lightPink            = new FeynColor(255, 182, 193);
  public static final FeynColor paleVioletRed        = new FeynColor(219, 112, 147);
  public static final FeynColor maroon               = new FeynColor(176,  48,  96);
  public static final FeynColor mediumVioletRed      = new FeynColor(199,  21, 133);
  public static final FeynColor violetRed            = new FeynColor(208,  32, 144);
  public static final FeynColor violet               = new FeynColor(238, 130, 238);
  public static final FeynColor plum                 = new FeynColor(221, 160, 221);
  public static final FeynColor orchid               = new FeynColor(218, 112, 214);
  public static final FeynColor mediumOrchid         = new FeynColor(186,  85, 211);
  public static final FeynColor darkOrchid           = new FeynColor(153,  50, 204);
  public static final FeynColor darkViolet           = new FeynColor(148,   0, 211);
  public static final FeynColor blueViolet           = new FeynColor(138,  43, 226);
  public static final FeynColor purple               = new FeynColor(160,  32, 240);
  public static final FeynColor mediumPurple         = new FeynColor(147, 112, 219);
  public static final FeynColor thistle              = new FeynColor(216, 191, 216);
  
  private static final double DARKENING_FACTOR = 0.7;
  
  private int r;
  private int g;
  private int b;
  private int a;

  private int rgba;

  public FeynColor(FeynColor color) {
    this(color.r, color.g, color.b, color.a);
  }

  public FeynColor(FeynColor color, int alpha) {
    this(color.r, color.g, color.b, alpha);
  }

  public FeynColor(int rgba) {
    this(ColorUtils.getRedFromRGBA(rgba), 
         ColorUtils.getGreenFromRGBA(rgba),  
         ColorUtils.getBlueFromRGBA(rgba),
         ColorUtils.getAlphaFromRGBA(rgba));
  }

  public FeynColor(int r, int g, int b) {
    this(r, g, b, 255);
  }

  public FeynColor(int r, int g, int b, int a) {
    this.r = Math.min(Math.max(r, 0), 255);
    this.g = Math.min(Math.max(g, 0), 255);
    this.b = Math.min(Math.max(b, 0), 255);
    this.a = Math.min(Math.max(a, 0), 255);
    
    this.rgba = ColorUtils.getRGBA(this.r, this.g, this.b, this.a);
  }

  public int getRed() {
    return r;
  }

  public int getGreen() {
    return g;
  }

  public int getBlue() {
    return b;
  }

  public int getAlpha() {
    return a;
  }

  public int getRGBA() {
    return rgba;
  }

  public boolean isTransparent() {
    return a < 255;
  }

  public FeynColor add(FeynColor that) {
    int R = this.r + that.r;
    int G = this.g + that.g;
    int B = this.b + that.b;
    int A = this.a;
    
    return new FeynColor(R, G, B, A);
  }

  public FeynColor sub(FeynColor that) {
    int R = this.r - that.r;
    int G = this.g - that.g;
    int B = this.b - that.b;
    int A = this.a;
    
    return new FeynColor(R, G, B, A);
  }

  public FeynColor mul(double factor) {
    return new FeynColor(MathUtils.roundToInt(r * factor), 
                         MathUtils.roundToInt(g * factor), 
                         MathUtils.roundToInt(b * factor), a);
  }

  public double luminance() {
    return 0.2125*r + 0.7154*g + 0.0721*b;
  }

  public FeynColor brighter() {
    int R = r;
    int G = g;
    int B = b;
    int A = a;

    int i = (int)(1.0 / (1.0-DARKENING_FACTOR));
    if (R == 0 && G == 0 && B == 0) {
        return new FeynColor(i, i, i, A);
    }
    if (R > 0 && R < i) R = i;
    if (G > 0 && G < i) G = i;
    if (B > 0 && B < i) B = i;

    return new FeynColor(Math.min((int)(R / DARKENING_FACTOR), 255),
                         Math.min((int)(G / DARKENING_FACTOR), 255),
                         Math.min((int)(B / DARKENING_FACTOR), 255), A);
  }

  public FeynColor darker() {
      return mul(DARKENING_FACTOR);
  }

  public FeynColor fadeTo(double d) {
    return new FeynColor(r, g, b, (int) (a * d));
  }

  public FeynColor opaque() {
    return new FeynColor(r, g, b);
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + r;
    result = 31 * result + g;
    result = 31 * result + b;
    result = 31 * result + a;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof FeynColor)) {
      return false;
    }
    
    var that = (FeynColor) obj;

    return this.r == that.r 
        && this.g == that.g
        && this.b == that.b
        && this.a == that.a;
  }

  @Override
  public String toString() {
    return String.format("%s:[r=%s,g=%s,b=%s,a=%s]", super.toString(), this.r, this.g, this.b, this.a);
  }

  public static FeynColor randomColor() { 
    return new FeynColor(XORShift.getInstance().randomInt(256), 
                         XORShift.getInstance().randomInt(256), 
                         XORShift.getInstance().randomInt(256));
  }
}