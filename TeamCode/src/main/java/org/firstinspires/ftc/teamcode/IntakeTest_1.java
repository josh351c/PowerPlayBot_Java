
package org.firstinspires.ftc.teamcode;

//import com.outoftheboxrobotics.photoncore.PhotonCore;
        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
        import com.qualcomm.robotcore.exception.RobotCoreException;
        import com.qualcomm.robotcore.hardware.CRServo;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.Gamepad;
        import com.qualcomm.robotcore.hardware.Servo;
        import com.qualcomm.robotcore.util.ElapsedTime;

        import org.firstinspires.ftc.robotcore.internal.camera.delegating.DelegatingCaptureSequence;
@TeleOp(name="IntakeTest_1")
public class IntakeTest_1 extends OpMode {

    private DcMotor front_left  = null;
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;
    private CRServo intake_servo = null;
    private Servo intake_tilt_servo = null;


    Gamepad currentGamepad1;
    Gamepad previousGamepad1;

    @Override
    public void init() {

        front_left   = hardwareMap.get(DcMotor.class, "front_left");
        front_right  = hardwareMap.get(DcMotor.class, "front_right");
        back_left    = hardwareMap.get(DcMotor.class, "back_left");
        back_right   = hardwareMap.get(DcMotor.class, "back_right");

        intake_servo = hardwareMap.get(CRServo.class, "intake_servo");
        intake_tilt_servo = hardwareMap.get(Servo.class, "intake_tilt_servo");

        front_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        front_right.setDirection(DcMotor.Direction.REVERSE);
        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();
    }


    @Override
    public void loop() {

        double drive = gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double twist = -gamepad1.right_stick_x;
        boolean intake_out = gamepad1.left_bumper;
        boolean intake_in = gamepad1.right_bumper;

        double[] speeds = {
                (drive + strafe + twist),
                (drive - strafe - twist),
                (drive - strafe + twist),
                (drive + strafe - twist)
        };
        double max = Math.abs(speeds[0]);
        for (int i = 0; i < speeds.length; i++) {
            if (max < Math.abs(speeds[i])) max = Math.abs(speeds[i]);
        }
        if (max > 1) {
            for (int i = 0; i < speeds.length; i++) speeds[i] /= max;
        }
//
//        front_left.setPower(speeds[0]);
//        front_right.setPower(speeds[1]);
//        back_left.setPower(speeds[2]);
//        back_right.setPower(speeds[3]);

        if (intake_in)
        {
            intake_servo.setPower(0.50);
            intake_tilt_servo.setPosition(0.80);
        }
        else
        {
            if (intake_out)
            {
                intake_servo.setPower(-1);
                intake_tilt_servo.setPosition(0.85);
            }
            else
            {
                intake_servo.setPower(0);
                intake_tilt_servo.setPosition(0.91);
            }
        }
    }
}
