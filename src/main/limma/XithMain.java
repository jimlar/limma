package limma;

import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

import net.jtank.input.KeyCode;
import net.jtank.input.MouseCode;
import org.xith3d.behaviors.TransformationDirectives;
import org.xith3d.behaviors.impl.RotatableGroup;
import org.xith3d.datatypes.Sized2i;
import org.xith3d.geometry.Cube;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loaders.texture.TextureStreamLocatorFile;
import org.xith3d.render.base.Xith3DEnvironment;
import org.xith3d.render.canvas.Canvas3DWrapper;
import org.xith3d.render.canvas.CanvasConstructionInfo;
import org.xith3d.render.loop.ExtRenderLoop;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.ui.hud.HUD;
import org.xith3d.ui.hud.utils.ButtonListener;
import org.xith3d.ui.hud.widgets.Button;

public class XithMain extends ExtRenderLoop implements ButtonListener {
    private Canvas3DWrapper canvas;

    public XithMain() {
        super(128);

        Xith3DEnvironment env = new Xith3DEnvironment(this);

        Tuple3f eyePosition = new Vector3f(0.0f, 0.0f, 5.0f);
        Tuple3f viewFocus = new Vector3f(0.0f, 0.0f, 0.0f);
        Tuple3f vecUp = new Vector3f(0.0f, 1.0f, 0.0f);
        env.getView().lookAt(eyePosition, viewFocus, vecUp);

        canvas = new Canvas3DWrapper(new CanvasConstructionInfo(800, 600, "My empty scene"));
        this.registerKeyboardAndMouse(canvas);

        TextureLoader.getInstance().addTextureStreamLocator(new TextureStreamLocatorFile("demo/textures"));
        env.addPerspectiveBranch(createScene());
        env.addParallelChild(createHUD(canvas));

        env.addCanvas(canvas);
    }

    public void onButtonClicked(Button button, Object userObject) {
        this.end();
    }

    private BranchGroup createScene() {
        Appearance app = new Appearance();
        app.setTexture(TextureLoader.getInstance().getTexture("stone.jpg"));
        Cube cube = new Cube(app, 3.0f);

        RotatableGroup rotatableGroup = new RotatableGroup(new TransformationDirectives(0.3f, 0.2f, 0.0f));
        rotatableGroup.addChild(cube);
        addAnimatableObject(rotatableGroup);
        return new BranchGroup(rotatableGroup);
    }

    private HUD createHUD(Sized2i canvasSize) {
        HUD hud = new HUD(canvasSize, 800f, 600f, this);
        // or: HUD hud = new HUD( canvasSize, this );
        hud.registerKeyboard(getInputManager().getKeyboard());
        hud.registerMouse(getInputManager().getMouse());

        Button button = new Button(50f, 30f, "<-");
        button.addButtonListener(this);
        hud.addWidget(button, 10, 500);

        Button button2 = new Button(50f, 30f, "->");
        button2.addButtonListener(this);
        hud.addWidget(button2, 70, 500);

        return (hud);
    }

    protected void onFPSCountIntervalHit(double fps) {
        super.onFPSCountIntervalHit(fps);
        canvas.setTitle("My empty scene, FPS: " + (int) fps);
    }

    protected void onKeyReleased(int key) {
        switch (key) {
            case KeyCode.VK_ESCAPE:
                this.end();
                break;
        }
    }

    protected void onMouseButtonPressed(int button, int x, int y) {
        switch (button) {
            case MouseCode.RIGHT_BUTTON:
                this.end();
                break;
        }
    }

    public static void main(String[] args) {
        XithMain xithMain = new XithMain();
        xithMain.begin();
    }
}
