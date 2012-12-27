using System;
using MonoTouch.Foundation;
using MonoTouch.UIKit;

using playn.ios;
using playn.core;
using fr.byob.game.memeduel.core;

namespace fr.byob.game.memeduel
{
  [Register ("AppDelegate")]
  public partial class AppDelegate : UIApplicationDelegate {
    public override bool FinishedLaunching (UIApplication app, NSDictionary options) {
      app.SetStatusBarHidden(true, true);
      IOSPlatform.register(app, IOSPlatform.SupportedOrients.PORTRAITS);
      PlayN.run(new Memeduel());
      return true;
    }
  }

  public class Application {
    static void Main (string[] args) {
      UIApplication.Main (args, null, "AppDelegate");
    }
  }
}
