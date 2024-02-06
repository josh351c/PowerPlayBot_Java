package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.arm.IntakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.auto.AutoCorrectHeadingDumb;
import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;
import org.firstinspires.ftc.teamcode.macros.auto.AutoOuttakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.auto.FindTeamElementPosition;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunExtraMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive;

@Autonomous(name="RedEndzone Shortside SENSING Auto", group="aCompete")
public class SmarterRedAuto extends OpMode {

    protected AutoMoveMacro.LateralDirection strafeDirection = AutoMoveMacro.LateralDirection.Forward;
    protected AutoMoveMacro.LateralDirection oppDirection = AutoMoveMacro.LateralDirection.Backward;

    SampleMecanumDrive drive;

    @Override
    final public void init() {
        RobotComponents.init(this.hardwareMap);

        drive = new SampleMecanumDrive(this.hardwareMap);

    }

    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void start() {

        ///MacroSequence.setTimeoutMs(90_000);
        telemetry.speak("Auto robot oh. Auto! Auto!");

        //
        runtime.reset();

        RobotComponents.imu.resetYaw();

        float scootDistanceMultiplier = 1f;

        float finalScootDistanceMultiplier = scootDistanceMultiplier;

        String TeamElementLocation = null;


                new RunActionMacro((o) -> {
                    RobotComponents.left_pixel_hold_servo.setPosition(
                            CompDrive.PIXEL_HOLD_POSITION
                    );

                    return false;
                });

                // Sense Team Element Spot and drive to center of spikemarks

                    TeamElementLocation = new FindTeamElementPosition().toString();
                    new RunActionMacro((o) -> {
                        new AutoMoveMacro(
                                drive, AutoMoveMacro.LateralDirection.Backward, 22.5, 0.5
                        );
                        return false;
                    });

                // Spikemark

                if (TeamElementLocation=="left") {
                    new RunActionMacro((o) -> {
                        new AutoCorrectHeadingDumb(
                                drive, 0, 0.04, -4.0
                        ).giveTimeout(750);
                        return false;
                    });

                } else if (TeamElementLocation=="center") {
                    new RunActionMacro((o) -> {
                    new AutoCorrectHeadingDumb(
                            drive, 0, 0.04, 8.0
                    ).giveTimeout(750);
                    return false;
                });

                } else { // right
                    new RunActionMacro((o) -> {
                    new AutoCorrectHeadingDumb(
                            drive, 0, 0.04, 4.0
                    ).giveTimeout(750);
                        return false;
                    });

                }

                    // Spits out pixel
                    RobotComponents.front_intake_motor.setPower(1);
                    new ArbitraryDelayMacro(200);
                    RobotComponents.front_intake_motor.setPower(0);

                // Drive to backboard

                if (TeamElementLocation=="left") { }

                else if (TeamElementLocation=="center") {
                    new RunActionMacro((o) -> {
                    new AutoCorrectHeadingDumb(
                            drive, 0, 0.04, -4.0
                    ).giveTimeout(750);
                    return false;
                });

                } else { // right
                    new RunActionMacro((o) -> {
                    new AutoCorrectHeadingDumb(
                            drive, 0, 0.04, 8.0
                    ).giveTimeout(750);
                    return false;
                });

                }
                new RunActionMacro((o) -> {
                    new AutoMoveMacro(
                        drive, AutoMoveMacro.LateralDirection.Backward,
                        35, 0.35
                    );
                        return false;
                        });

                // Align

                    if (TeamElementLocation=="left") {
                        new RunActionMacro((o) -> {
                        new AutoMoveMacro(drive, AutoMoveMacro.LateralDirection.Left,
                               3, 0.35);
                            return false;
                        });

                    }

                    else if (TeamElementLocation=="center") { }

                    else { // right
                        new RunActionMacro((o) -> {
                        new AutoMoveMacro(drive, AutoMoveMacro.LateralDirection.Right,
                                3, 0.35);
                            return false;
                        });

                    }

                // Place and Scoot

                new RunActionMacro((o) -> {
                    CompDrive.TOWER_UP_SEQUENCE.get().append(
                            new ArbitraryDelayMacro(700),
                            new RunActionMacro((o2) -> {
                                RobotComponents.left_pixel_hold_servo.setPosition(
                                        CompDrive.PIXEL_RELEASE_POSITION
                                );
                                MacroSequence.begin("Scoot Back",
                                        new ArbitraryDelayMacro(700),
                                        new RunActionMacro((o3) -> {
                                            telemetry.speak("beep beep beep beep");
                                            RobotComponents.front_intake_motor.setPower(1);
                                            CompDrive.TOWER_DOWN_SEQUENCE.get().append(
                                                            new ArbitraryDelayMacro(200),
                                                            new AutoMoveMacro(
                                                                    drive, AutoMoveMacro.LateralDirection.Forward,
                                                                    4, 0.5
                                                            ),
                                                            new AutoCorrectHeadingDumb(
                                                                    drive, 0, 0.04, 2.5
                                                            ).giveTimeout(750),
                                                            new AutoMoveMacro(
                                                                    drive, AutoMoveMacro.LateralDirection.Left,
                                                                    18 * finalScootDistanceMultiplier, 0.4
                                                            ),
                                                            new RunActionMacro((o4) -> {
                                                                MacroSequence.begin("stop intake",
                                                                        new ArbitraryDelayMacro(3000),
                                                                        new RunActionMacro((o5) -> {
                                                                            RobotComponents.front_intake_motor.setPower(0);
                                                                            return false;
                                                                        })
                                                                );
                                                                return false;
                                                            })
                                                    )
                                                    .start();


                                            return false;
                                        })


                                );
                                return false;
                            })

                    ).start();

                    return false;
                }
        );





    }

    @Override
    public void loop() {
        RobotComponents.tickSystems(this);
        MacroSequence.appendDebugTo(telemetry);
        YawPitchRollAngles orientation = RobotComponents.imu.getRobotYawPitchRollAngles();
        telemetry.addData("Orientation: ",
                "H (Zrot): " + orientation.getYaw(AngleUnit.DEGREES) +
                        ", P (Xrot): " + orientation.getPitch(AngleUnit.DEGREES) +
                        ", R (Yrot): " + orientation.getRoll(AngleUnit.DEGREES));
    }

}
