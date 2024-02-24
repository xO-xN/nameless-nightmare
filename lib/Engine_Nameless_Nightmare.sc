Engine_Nameless_Nightmare : CroneEngine {

    var <bufferL;
	var <bufferR;
	var pg;
	var effect;
	var mixBus;

	var mercury, venus, earth, mars, jupiter, saturn, uranus, neptune;

	*new { arg context, doneCallback;
		^super.new(context, doneCallback);
	}

    readBuf { arg path;
		if (File.exists(path), {
			var numChannels;
			var buffer;

			numChannels = SoundFile.use(path.asString(), { |f| f.numChannels });

			buffer = Buffer.readChannel(context.server, path, 0, -1, [0], { |b|
				mercury.set(\buf_l, b);
				venus.set(\buf_l, b);
				earth.set(\buf_l, b);
				mars.set(\buf_l, b);
				jupiter.set(\buf_l, b);
				saturn.set(\buf_l, b);
				uranus.set(\buf_l, b);
				neptune.set(\buf_l, b);
				bufferL.free;
				bufferL = b;
			});

			if (numChannels > 1, {
				buffer = Buffer.readChannel(context.server, path, 0, -1, [1], { |b|
					mercury.set(\buf_r, b);
					venus.set(\buf_r, b);
					earth.set(\buf_r, b);
					mars.set(\buf_r, b);
					jupiter.set(\buf_r, b);
					saturn.set(\buf_r, b);
					uranus.set(\buf_r, b);
					neptune.set(\buf_r, b);
					bufferR.free;
					bufferR = b;
				});
			}, {
				mercury.set(\buf_r, buffer);
				venus.set(\buf_r, buffer);
				earth.set(\buf_r, buffer);
				mars.set(\buf_r, buffer);
				jupiter.set(\buf_r, buffer);
				saturn.set(\buf_r, buffer);
				uranus.set(\buf_r, buffer);
				neptune.set(\buf_r, buffer);
				bufferR.free;
				bufferR = buffer;
			});
		});
	}

    alloc {
        bufferL = Buffer.alloc(
				context.server,
				context.server.sampleRate * 1,
		);
		bufferR = Buffer.alloc(
				context.server,
				context.server.sampleRate * 1,
		);

        SynthDef(\mercury, {
			arg buf_l, buf_r, mod=1, gate=0, amp=0.8, out;
			var sig, env;
			sig = GrainBuf.ar(
				2,
				Impulse.ar(SinOsc.ar([0.015, 0.013], [0.3, 0.6], 0.2, LFNoise1.ar(44, 4, 44))),
				LFNoise0.ar(1, 0.0024, 0.0856),
				[buf_l, buf_r],
				[0.24 + SinOsc.ar(6, 0, 0.08), 0.123 + SinOsc.ar(4, 0, 0.12)],
				[0.2 + LFSaw.ar(0.04, 0, 0.04), 0.4 + Dust.ar(0.4)],
				mul: [LFNoise1.ar(1, 0.5, 0.5), LFNoise1.ar(1, 0.5, 0.5)]
			);
			sig = sig.sum;
			sig = LPF.ar(sig, 880 + (LFNoise1.ar(24, 400) * mod));
			sig = sig + PitchShift.ar(sig, 0.2, [LFNoise1.ar(0.01467, 0.015, 2 * mod), LFNoise1.ar(0.01427, 0.015, 2 * mod)], 0, 0.22);
			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			sig = sig * env;

			sig = Limiter.ar(sig);

			Out.ar(out, sig);
		}).add;

		SynthDef(\venus, {
			arg buf_l, buf_r, gate=0, amp=0.8, out;
			var sig, env;
			sig = GrainBuf.ar(
				2,
				Impulse.ar(LFNoise1.ar(0.0679, 12, 69)),
				LFSaw.kr(0.56 + LFNoise1.ar(0.06, 0.24), [-1, 0.8], 0.01, 0.03),
				[buf_l, buf_r],
				[0.5, 1.5, 2.25],
				SinOsc.ar(0.00235, [0.254, 0.635], 0.25, [0.33333, 0.73999]),
				pan: [LFNoise1.ar(0.2, 0.05, 0.5),LFNoise1.ar(0.2, 0.25, 0.5),LFNoise1.ar(0.2, 0.45, 0.5)]
			);
			sig = Splay.ar(sig);
			sig = sig.tanh;
			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			sig = sig * env;

			sig = Limiter.ar(sig);

			Out.ar(out, sig);
		}).add;

		SynthDef(\earth, {
			arg buf_l, buf_r, gate=0, amp=0.8, out;
			var sig, env;
			sig = GrainBuf.ar(
				2,
				Impulse.ar(44),
				SinOsc.ar(0.00815, 0.499, 0.0083, 0.0136),
				[buf_l, buf_r],
				SinOsc.ar(0.00486, 0.254, 0.0125, 0.934),
				[SinOsc.ar(0.0032 + LFNoise1.kr(0.02, 0.001), 0.2, 0.4, 0.5), SinOsc.ar(0.0034 + LFNoise1.kr(0.02, 0.001), 0.6, 0.3, 0.6)]

			);
			sig = sig * -3.dbamp;
			sig = sig.tanh;
			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			sig = sig * env;

			sig = Limiter.ar(sig);

			Out.ar(out, sig);
		}).add;

		SynthDef(\mars, {
			arg buf_l, buf_r, gate=0, amp=0.8, out;
			var sig, env;
			sig = GrainBuf.ar(
				2,
				Impulse.ar(53 + [LFNoise1.ar(0.02, 1.4), LFNoise1.ar(0.02, 1.4)]),
				SinOsc.ar(0.029, 0.6, 0.01, 0.023) + LFNoise1.ar(0.5, 0.008),
				[buf_l, buf_r],
				LFSaw.ar(0.00747, 0, 0.66, 0.7) + LFPulse.ar(LFNoise0.ar(2, 0.05, 0.06), 0.5, 0.01, 3, 0.6),
			);
			sig = sig * -3.dbamp;
			sig = sig.tanh;
			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			sig = sig * env;

			sig = Limiter.ar(sig);
			Out.ar(out, sig);
		}).add;

		SynthDef(\jupiter, {
			arg buf_l, buf_r, gate=0, amp=0.8, out;
			var sig, env;
			sig = GrainBuf.ar(
				2,
				Impulse.ar(LFSaw.ar(0.06, mul: 4, add: 8)),
				SinOsc.ar(0.008333, 0, 0.05, 0.1),
				[buf_l, buf_r],
				0.7 + LFNoise1.ar(0.1, 0.3),
				Impulse.ar(LFSaw.ar(0.0012, mul: 5, add: 5.1) + LFNoise1.ar(0.08333, 0.2), mul: LFSaw.ar(-0.01, mul: 5, add: 5.1), add: LFNoise0.ar(0.12, 0.1, 0.4)),
				pan: LFNoise0.ar(LFSaw.ar(0.06, mul: 4, add: 8), 0.4)
			);
			sig = RLPF.ar(sig, LFSaw.ar(0.01, mul: 800, add: 1600), 5);
			//sig = sig * -3.dbamp;

			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			sig = sig * env;

			sig = Limiter.ar(sig);
			Out.ar(out, sig);
		}).add;

		SynthDef(\saturn, {
			arg buf_l, buf_r, gate=0, amp=0.8, out;
			var sig, env;
			sig = GrainBuf.ar(
				2,
				Dust.ar(1.2 + LFNoise0.ar(1, 0.3)),
				SinOsc.ar(0.1, 0, [0.05, 0.06], LFSaw.ar(0.02, 0, 0.06, 0.1)),
				[buf_l, buf_r],
				2.6,
				Phasor.ar(Impulse.ar(12), 1 * BufRateScale.ir(buf_l), [0.22, 0.2], LFSaw.ar(0.06, 0, 0.07, 0.3), [0.22, 0.2]),
				pan: LFNoise0.ar(1, 0.8)
			);
			sig = sig * -3.dbamp;

			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			sig = sig * env;

			sig = Limiter.ar(sig);
			Out.ar(out, sig);
		}).add;

		SynthDef(\uranus, {
			arg buf_l, buf_r, gate=0, amp=0.8, out;
			var sig, env;
			sig = GrainBuf.ar(
				2,
				Dust.ar(40),
				SinOsc.ar(0.1, 0, 0.05, 0.6),
				[buf_l, buf_r],
				SinOsc.ar(0.006, mul: 0.2, add: -1),
				Impulse.ar(Impulse.ar(10,0,5,5), mul: 0.03, add: SinOsc.ar(0.25, mul: 0.02, add: 0.7))
			);
			sig = SinOsc.ar([LFNoise0.ar(0.1, 50, 300), LFNoise0.ar(0.1, 50, 300)], mul: sig);
			sig = sig * -3.dbamp;

			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			sig = sig * env;

			sig = Limiter.ar(sig);
			Out.ar(out, sig);
		}).add;

		SynthDef(\neptune, {
			arg buf_l, buf_r, gate=0, amp=0.8, out;
			var sig, env;
			sig = GrainBuf.ar(
				2,
				Impulse.ar(LFTri.kr(0.2, 0, 8, 12)),
				LFSaw.ar(LFPulse.kr(Dust.ar(0.1), 0, LFNoise0.ar(0.002, 0.2, 0.4), 0.8) + 1.2, 0, 0.038, 0.102),
				[buf_l, buf_r],
				LFNoise0.ar(1, 0.128, 0.9),
				[LFSaw.ar(LFNoise0.ar(0.1, 100, 104), 0, 0.1, -0.3), LFSaw.ar(LFNoise0.ar(0.1, 100, 104), 0, 0.15, -0.75)],
			);
			sig = Pan2.ar(sig, LFNoise1.ar(1, 0.9));
			sig = sig * -3.dbamp;

			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			sig = sig * env;

			sig = Limiter.ar(sig);
			Out.ar(out, sig);
		}).add;



		SynthDef(\effect, {
			arg in, out;
			var sig = In.ar(in, 2);
			sig = FreeVerb2.ar(sig[0], sig[1], 0.4, 0.2, 0.2);
			sig = Limiter.ar(sig);
			Out.ar(out, sig);
		}).add;

		context.server.sync;

		mixBus = Bus.audio(context.server, 2);

		effect = Synth.new(\effect, [\in, mixBus, \out, context.out_b], target: context.xg);

		pg = ParGroup.head(context.xg);

		mercury = Synth.new(\mercury, [
			\out, mixBus,
			\buf_l, bufferL,
			\buf_r, bufferR,
			\mod, 1,
			\amp, 0.8,
			\gate, 0
		], target: pg);

		venus = Synth.new(\venus, [
			\out, mixBus,
			\buf_l, bufferL,
			\buf_r, bufferR,
			\amp, 0.8,
			\gate, 0
		], target: pg);

		earth = Synth.new(\earth, [
			\out, mixBus,
			\buf_l, bufferL,
			\buf_r, bufferR,
			\amp, 0.8,
			\gate, 0
		], target: pg);

		mars = Synth.new(\mars, [
			\out, mixBus,
			\buf_l, bufferL,
			\buf_r, bufferR,
			\amp, 0.8,
			\gate, 0
		], target: pg);

		jupiter = Synth.new(\jupiter, [
			\out, mixBus,
			\buf_l, bufferL,
			\buf_r, bufferR,
			\amp, 0.8,
			\gate, 0
		], target: pg);

		saturn = Synth.new(\saturn, [
			\out, mixBus,
			\buf_l, bufferL,
			\buf_r, bufferR,
			\amp, 0.8,
			\gate, 0
		], target: pg);

		uranus = Synth.new(\uranus, [
			\out, mixBus,
			\buf_l, bufferL,
			\buf_r, bufferR,
			\amp, 0.8,
			\gate, 0
		], target: pg);

		neptune = Synth.new(\neptune, [
			\out, mixBus,
			\buf_l, bufferL,
			\buf_r, bufferR,
			\amp, 0.8,
			\gate, 0
		], target: pg);


		context.server.sync;


		this.addCommand("read", "s", { arg msg;
			this.readBuf(msg[1])
		});

		this.addCommand("mercuryGate", "i", { arg msg;
			mercury.set(\gate, msg[1])
		});

		this.addCommand("mercurySelfAmp", "f", { arg msg;
			mercury.set(\amp, msg[1])
		});


		this.addCommand("venusGate", "i", { arg msg;
			venus.set(\gate, msg[1])
		});

		this.addCommand("venusSelfAmp", "f", { arg msg;
			venus.set(\amp, msg[1])
		});


		this.addCommand("earthGate", "i", { arg msg;
			earth.set(\gate, msg[1])
		});

		this.addCommand("earthSelfAmp", "f", { arg msg;
			earth.set(\amp, msg[1])
		});


		this.addCommand("marsGate", "i", { arg msg;
			mars.set(\gate, msg[1])
		});

		this.addCommand("marsSelfAmp", "f", { arg msg;
			mars.set(\amp, msg[1])
		});

		this.addCommand("jupiterGate", "i", { arg msg;
			jupiter.set(\gate, msg[1])
		});

		this.addCommand("jupiterSelfAmp", "f", { arg msg;
			jupiter.set(\amp, msg[1])
		});


		this.addCommand("saturnGate", "i", { arg msg;
			saturn.set(\gate, msg[1])
		});

		this.addCommand("saturnSelfAmp", "f", { arg msg;
			saturn.set(\amp, msg[1])
		});


		this.addCommand("uranusGate", "i", { arg msg;
			uranus.set(\gate, msg[1])
		});

		this.addCommand("uranusSelfAmp", "f", { arg msg;
			uranus.set(\amp, msg[1])
		});

		this.addCommand("neptuneGate", "i", { arg msg;
			neptune.set(\gate, msg[1])
		});

		this.addCommand("neptuneSelfAmp", "f", { arg msg;
			neptune.set(\amp, msg[1])
		});


	}

	free {
		bufferL.free;
		bufferR.free;
		mercury.free;
		venus.free;
		earth.free;
		mars.free;
		jupiter.free;
		saturn.free;
		uranus.free;
		neptune.free;
	}
}