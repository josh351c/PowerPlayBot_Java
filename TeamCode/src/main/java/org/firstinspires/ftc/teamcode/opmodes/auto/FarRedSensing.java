package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.RobotComponents;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.PrimaryDetectionPipeline;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.Webcam;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import static org.firstinspires.ftc.teamcode.components.RobotComponents.*;
import static org.firstinspires.ftc.teamcode.components.RobotConstants.*;



@Autonomous(name="RedEndzone FAR SENSING Auto", group="aCompete")
public class FarRedSensing extends LinearOpMode {

    public DcMotor tower_motor = RobotComponents.tower_motor;
    public Servo   wrist_servo = RobotComponents.wrist_servo;
    public Servo   bucket_servo = RobotComponents.wrist_servo;

    public Webcam webcam = new Webcam();
    public static final double PIXEL_RELEASE_POSITION = 0.5;
    public static final double PIXEL_HOLD_POSITION = 1.0;




    public void runOpMode() throws InterruptedException {

        RobotComponents.init(hardwareMap);
        final double TowerGoalPosition = (RobotComponents.tower_motor.getCurrentPosition() - 1288);

        RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_HOLD_POSITION);
        RobotComponents.right_pixel_hold_servo.setPosition( PIXEL_HOLD_POSITION);

        RobotComponents.extendo_servo.setPosition(0.7);



        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        webcam.initCamera(hardwareMap, PrimaryDetectionPipeline.Color.RED);

        int ElementLocation = 0;

        while (opModeInInit()) {
            telemetry.addData("Location: ", webcam.getLocation());
            telemetry.update();
            if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.CENTER) {
                telemetry.addLine("IS CENTER");
                ElementLocation = 1;
            } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.RIGHT) {
                telemetry.addLine("IS RIGHT");
                ElementLocation = 2;
            } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.LEFT) {
                telemetry.addLine("IS LEFT");
                ElementLocation = 0;
            }
        }

        waitForStart();
        if(isStopRequested()) {
            return;
        }


        Pose2d startPose = new Pose2d(11, -61, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

//RIGHT
        TrajectorySequence rightCloseAuto = drive.trajectorySequenceBuilder(startPose)
                .forward(28)
                .turn(Math.toRadians(-90))
                .waitSeconds(.15)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(.28)) // Spit out
                .waitSeconds(.45)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .waitSeconds(.15)
                .back(2)
                .turn(Math.toRadians(90))
                .back(24)
                .turn(Math.toRadians(90))
                .back(93-8)
                .strafeRight(19)
                .back(6)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .waitSeconds(.70)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.55)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_TELEOP_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_TELEOP_POSITION))
                .waitSeconds(.25)
                .forward(2)
                .strafeLeft(22)
                .back(8)
                .build();

// LEFT

        TrajectorySequence closeLeftAuto = drive.trajectorySequenceBuilder(startPose)
                .forward(28)
                .waitSeconds(.15)
                .turn(Math.toRadians(90))
                .back(4)
                .waitSeconds(.15)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(.3)) // Spit out
                .waitSeconds(.45)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .waitSeconds(.15)
                .strafeLeft(24)
                .back(90-8)
                .strafeRight(31.5)
                .back(3)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .waitSeconds(.70)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.55)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_TELEOP_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_TELEOP_POSITION))
                .waitSeconds(.25)
                .forward(5)
                .strafeLeft(32)
                .back(8)
                .build();

        // CENTER

        TrajectorySequence closeCenterAuto = drive.trajectorySequenceBuilder(startPose)
                .forward(35)
                .waitSeconds(.2)
                .back(8)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(.25)) // Spit out
                .waitSeconds(.45)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .back(3.5)
                .turn(Math.toRadians(90))
                .strafeRight(3.5)
                .back(98-8)
                .strafeLeft(5)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .waitSeconds(.70)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.55)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_TELEOP_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_TELEOP_POSITION))
                .waitSeconds(.25)
                .forward(3)
                .strafeLeft(24.5)
                .back(8)
                .build();




        if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.LEFT) {
            drive.followTrajectorySequence(closeLeftAuto);
        } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.CENTER) {
            drive.followTrajectorySequence(closeCenterAuto);
        } else {
            drive.followTrajectorySequence(rightCloseAuto);
        }
        RobotComponents.extendo_servo.setPosition(0.7);

    }




}

