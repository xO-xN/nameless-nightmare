Engine_Nameless_Nightmare : CroneEngine {

    var <bufferL;
	var <bufferR;
	var pg;
	var effect;
	var mixBus;

	var mercury, venus, earth, mars, jupiter, saturn, uranus, neptune, planetx;

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

		//Mercury
		//vulcan_pow(min: 0, max: 100);
        SynthDef(\mercury, {
			arg buf_l, buf_r, out, gate=0, amp=0.8, vulcan_pow=0;
			var sig, env;
			sig = GrainBuf.ar(
				2,
				Impulse.ar(SinOsc.ar(0.015, [0.333, 0.666], 0.2, 44)),
				[0.02, 0.028 + LFNoise1.kr(1.2, 0.002)],
				[buf_l, buf_r],
				[0.24 + SinOsc.ar(0.06 + (vulcan_pow/300), 0, 0.08, vulcan_pow/300), 0.24 + SinOsc.ar(0.04 + (vulcan_pow/100), 0, 0.12, vulcan_pow/100)],
				[0.2 + LFSaw.ar(0.04, 0, 0.15), 0.8 + Dust.ar(1.5, 0.15)],
				pan: [-1*(vulcan_pow/100), 1*(vulcan_pow/100)]
			);
			sig = LPF.ar(sig, [800 + vulcan_pow * 4, 800 + (LFNoise1.kr(24, 200) * vulcan_pow/100)]);
			sig = Splay.ar(sig, vulcan_pow/100);

			sig = sig * -3.dbamp;
			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			sig = sig * env;

			sig = Limiter.ar(sig);

			Out.ar(out, sig);
		}).add;


		//Venus
		//vulcan_pow(min: 0, max: 100);
		SynthDef(\venus, {
			arg buf_l, buf_r, out, gate=0, amp=0.8, zoozve_pow=0;
			var sig, env;
			sig = GrainBuf.ar(
				2,
				Impulse.ar(LFNoise1.kr(0.0679, 12, 69)),
				LFSaw.kr(0.56 + LFNoise1.kr(0.06, 0.24), [-1, -0.4, 0.8], 0.01, 0.03),
				[buf_l, buf_r, buf_l],
				[0.5, 1.5, 2.25],
				SinOsc.ar(0.00235, [0.34, 0.254, 0.635], 0.25, [0.33333, 0.66666, 0.75999]),
				pan: [LFNoise1.kr(0.2, 0.05, 0.5),LFNoise1.kr(0.2, 0.25, 0.5),LFNoise1.kr(0.2, 0.45, 0.5)]
			);
			sig = Splay.ar(sig);
			sig = sig + PitchShift.ar(sig, 0.2, [LFNoise1.kr(0.01467, 0.015, zoozve_pow/100), LFNoise1.kr(0.01427, 0.015, zoozve_pow/100)], 0, 0.22, zoozve_pow/100);
			sig = sig.tanh;
			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			sig = sig * env;

			sig = Limiter.ar(sig);

			Out.ar(out, sig);
		}).add;


		//Earth
		//moon_pow(min:0, max:100);
		//satellites_pow;
		SynthDef(\earth, {
			arg buf_l, buf_r, out, gate=0, amp=0.8, moon_pow=0, satellites_pow=0;
			var sig, sig1, env, env1, moon_amp, moon_sel, earthshake, moon_theia, theiashake;

			moon_sel = ((moon_pow-50)/50).atan2(0.1).round(1) + 1;
			moon_amp = Select.kr(moon_sel, [moon_pow/100,0.4,moon_pow/120]);
			earthshake = Select.kr(moon_sel, [0-(moon_pow/24),LFNoise1.kr(16,20),(moon_pow/24)-12]);
			moon_theia = Select.kr(moon_sel, [0-(moon_pow/50),1,(moon_pow/100)-2]);
			theiashake = Select.kr(moon_sel, [0,0,(moon_pow/24)-12]);

			sig = GrainBuf.ar(
				2,
				Impulse.ar(44 + earthshake),
				SinOsc.ar(0.00815, 0.499, 0.0083, 0.0166 + (satellites_pow*0.0004)),
				[buf_l, buf_r],
				SinOsc.ar(0.00486, 0.254, 0.0125, 0.934),
				[SinOsc.ar(0.0032 + LFNoise1.kr(0.02, 0.001), 0.2, 0.4, 0.5), SinOsc.ar(0.0034 + LFNoise1.kr(0.02, 0.001), 0.6, 0.3, 0.6)]
			);
			sig = sig * -3.dbamp;
			sig = sig.tanh;

			sig1 = GrainBuf.ar(
				2,
				Impulse.kr(44 + theiashake),
				SinOsc.ar(0.00815, 0.499, 0.0083, 0.0136 + (satellites_pow*0.0003)),
				[buf_l, buf_r],
				SinOsc.ar(0.00486, 0.254, 0.0125, 1.934 + moon_theia),
				[SinOsc.ar(0.0032 + LFNoise1.kr(0.02, 0.001), 0.2, 0.4, 0.5), SinOsc.ar(0.0034 + LFNoise1.kr(0.02, 0.001), 0.6, 0.3, 0.6)]
			);
			sig1 = sig1 * -3.dbamp;
			sig1 = sig1.tanh;

			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			env1 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: moon_amp);
			sig = (sig * env) + (sig1 * env1);

			sig = Limiter.ar(sig);

			Out.ar(out, sig);
		}).add;


		//Mars
		//phobos_pow(min:0, max:100);
		//deimos_pow;
		SynthDef(\mars, {
			arg buf_l, buf_r, out, gate=0, amp=0.8, phobos_pow=0, deimos_pow=0;
			var sig, sig1, sig2, env, env1, env2, phobos_amp, phobos_sel, deimos_amp, deimos_sel, marsshake, phobosshake;

			phobos_sel = ((phobos_pow-50)/50).atan2(0.1).round(1) + 1;
			phobos_amp = Select.kr(phobos_sel, [phobos_pow/40,0,phobos_pow/150]);

			deimos_sel = ((deimos_pow-50)/50).atan2(0.5).round(1) + 1;
			deimos_amp = Select.kr(deimos_sel, [deimos_pow/40,1,((100-deimos_pow)/40).linlin(0,1,1,0)]);

			marsshake = Select.kr(phobos_sel, [0,20,13]);
			phobosshake = Select.ar(phobos_sel, [Impulse.ar(phobos_pow/2),Impulse.ar(1),Dust.ar(phobos_pow)]);

			sig = GrainBuf.ar(
				2,
				Impulse.ar(53 - marsshake + [LFNoise1.ar(0.02, 1.4), LFNoise1.ar(0.02, 1.4)]),
				SinOsc.ar(0.029, 0.6, 0.01, 0.023) + LFNoise1.ar(0.5, 0.008) + (marsshake*0.003),
				[buf_l, buf_r],
				LFSaw.ar(0.00747, 0, 0.66, 0.7) + LFPulse.ar(LFNoise0.ar(2, 0.05, 0.06), 0.5, 0.01, 3, 0.6),
				LFSaw.ar(0.00025, -1, 0.49, 0.5)
			);
			sig = sig * -3.dbamp;
			sig = sig.tanh;

			sig1 = GrainBuf.ar(
				2,
				phobosshake,
				1.2/phobos_pow,
				buf_l,
				1 + ((phobos_pow-50)*0.2),
				LFSaw.ar(0.00025, -1, 0.49, 0.5),
				pan: LFNoise1.kr(20)
			);
			sig1 = sig1 * -3.dbamp;
			sig1 = sig1.tanh;

			sig2 = GrainBuf.ar(
				2,
				Impulse.ar(10),
				0.1,
				buf_r,
				0.8 + SinOsc.ar(0.024, 0, 0.08),
				deimos_pow/100
			);
			sig2 = sig2 * -3.dbamp;
			sig2 = sig2.tanh;

			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			env1 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: phobos_amp);
			env2 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: deimos_amp);
			sig = (sig * env) + (sig1 * env1) + (sig2 * env2);

			sig = Limiter.ar(sig);
			Out.ar(out, sig);
		}).add;


		//Jupiter
		//moons_pow(min:0, max:100);
		//ring_pow();
		SynthDef(\jupiter, {
			arg buf_l, buf_r, out, gate=0, amp=0.8, moons_pow=0, ring_pow=0;
			var sig, sig1, env, env1, moons_mod, moons_amp;

			moons_amp = Select.kr((moons_pow/100).round(1), [moons_pow/50, 1]);
			moons_mod = Select.kr((moons_pow/100).round(1), [0, 0.0022*(moons_pow-50)]);

			sig = GrainBuf.ar(
				2,
				Impulse.ar(LFSaw.ar(0.06, mul: 4, add: 8)),
				SinOsc.ar(0.008333, 0, 0.05, 0.1 + (ring_pow * 0.0008)),
				[buf_l, buf_r],
				0.7 + LFNoise1.ar(0.1, 0.3),
				Impulse.ar(LFSaw.ar(0.0012, mul: 5, add: 5.1) + LFNoise1.ar(0.08333, 0.2), mul: LFSaw.ar(-0.01, mul: 5, add: 5.1), add: LFNoise0.ar(0.12, 0.1, 0.4)),
				pan: LFNoise0.ar(LFSaw.ar(0.06, mul: 4, add: 8), 0.4)
			);
			sig = RLPF.ar(sig, LFSaw.ar(0.01, mul: 800, add: 1600), 2);
			sig = sig * -1.dbamp;

			sig1 = GrainBuf.ar(
				2,
				Impulse.ar(LFSaw.ar(0.06, add: [1.001, 2.008, 4.019, 9.4])),
				SinOsc.ar(0.008333, 0, 0.05, 0.1) + moons_mod,
				[buf_l, buf_r, buf_l, buf_r],
				1 + moons_mod,
				[0.2, 0.4, 0.6, 0.8],
				pan: SinOsc.kr(0.024 + moons_mod, [0, pi/2, pi, 3pi/2])
			);
			sig1 = Splay.ar(sig1, 0.9, 0.5);

			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			env1 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: moons_amp);

			sig = (sig * env) + (sig1 * env1);

			sig = Limiter.ar(sig);
			Out.ar(out, sig);
		}).add;


		//Saturn
		//ring_pow(min:0, max:100);
		//mimas_pow();
		//rhea_pow();
		//titan_pow();
		SynthDef(\saturn, {
			arg buf_l, buf_r, out, gate=0, amp=0.8, ring_pow=0, mimas_pow=0, rhea_pow=0, titan_pow=0;
			var sig, sig1, sig2, sig3, sig4, env, env1, env2, env3;

			sig = GrainBuf.ar(
				2,
				Dust.ar(1.2 + LFNoise0.ar(1, 0.3)),
				SinOsc.ar(0.1, 0, [0.05, 0.06], LFSaw.ar(0.02, 0, 0.06, 0.1)),
				[buf_l, buf_r],
				2.6,
				Phasor.ar(Impulse.ar(12), 4000, [0.22, 0.2], LFSaw.ar(0.06, 0, 0.07, 0.3), [0.22, 0.2]),
				pan: LFNoise0.ar(1, 0.8)
			);
			sig = sig * -3.dbamp;

			sig4 = DelayN.ar(sig, 0.8, 0.68, rhea_pow/100);
			sig4 = PitchShift.ar(sig4, 0.3, 2);
			sig4 = Pan2.ar(sig4, LFNoise1.kr(0.24));

			sig1 = GrainBuf.ar(
				2,
				Impulse.ar(SinOsc.kr(0.024, [0, pi/3, 2pi/3, pi, 4pi/3, 5pi/3], 10, 10)),
				0.012 - (mimas_pow*0.00006),
				[buf_l, buf_r, buf_l, buf_r ,buf_l, buf_r],
				SinOsc.kr(0.024, [0, pi/3, 2pi/3, pi, 4pi/3, 5pi/3], 0.5, 0),
				[0.15, 0.3, 0.45, 0.6, 0.75, 0.9],
				pan: SinOsc.kr(0.024, [0, pi/3, 2pi/3, pi, 4pi/3, 5pi/3])
			);
			sig1 = Splay.ar(sig1, 0.9, 0.9);

			sig2 = GrainBuf.ar(
				2,
				Impulse.ar(LFSaw.kr(0.453, -1, 7, 8)),
				0.008,
				[buf_l, buf_r],
				0.2,
				LFSaw.kr(0.0075, 0, 0.45, 0.5)
			);

			sig3 = GrainBuf.ar(
				2,
				Impulse.ar(44),
				SinOsc.ar(0.00815, 0.499, 0.0083, 0.0166),
				[buf_l, buf_r],
				SinOsc.ar(0.00486, 0.254, 0.0125, 1.28),
				[SinOsc.ar(0.0032 + LFNoise1.kr(0.02, 0.001), 0.2, 0.4, 0.5), SinOsc.ar(0.0034 + LFNoise1.kr(0.02, 0.001), 0.6, 0.3, 0.6)]
			);
			sig3 = Splay.ar(sig3, 0.3);
			sig3 = sig3 * -5.dbamp;


			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			env1 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: ring_pow/100);
			env2 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: mimas_pow/100);
			env3 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: titan_pow/100);

			sig = (sig * env) + (sig1 * env1) + (sig2 * env2) + sig4 + (sig3 * env3);
			sig = Limiter.ar(sig);
			Out.ar(out, sig);
		}).add;


		//Uranus
		//titania_pow(min:0, max:100);
		//oberon_pow();
		//ring_pow();
		SynthDef(\uranus, {
			arg buf_l, buf_r, out, gate=0, amp=0.8, titania_pow=0, oberon_pow=0, ring_pow=0;
			var sig, sig1, sig2, sig3, env, env1, env2, env3, noise1, noise2;

			noise1 = LFNoise0.kr(0.126, 50, 300 + titania_pow);
			noise2 = LFNoise0.kr(0.1254, 50, 300 + oberon_pow);

			sig = GrainBuf.ar(
				2,
				Dust.ar(40),
				SinOsc.ar(0.1, 0, 0.05, 0.6),
				[buf_l, buf_r],
				SinOsc.ar(0.006, mul: 0.2, add: -1),
				Impulse.ar(Impulse.ar(10,0,5,5), mul: 0.03, add: SinOsc.ar(0.25, mul: 0.02, add: 0.7))
			);
			sig = SinOsc.ar([noise1, noise2], mul: sig);
			sig = Splay.ar(sig, 0.6);
			sig = sig * -5.dbamp;
			sig = LPF.ar(sig, 8000);

			sig1 = GrainBuf.ar(
				2,
				Dust.ar(40),
				SinOsc.ar(0.1, 0, 0.05, 0.6),
				buf_l,
				1,
				Impulse.ar(Impulse.ar(10,0,5,5), mul: 0.03, add: SinOsc.ar(0.25, mul: 0.02, add: 0.2)),
			);
			sig1 = Pan2.ar(sig1, -0.8);
			sig1 = sig1 * -5.dbamp;

			sig2 = GrainBuf.ar(
				2,
				Dust.ar(40),
				SinOsc.ar(0.1, 0, 0.05, 0.6),
				buf_r,
				1,
				Impulse.ar(Impulse.ar(10,0,5,5), mul: 0.03, add: SinOsc.ar(0.25, mul: 0.02, add: 0.4)),
			);
			sig2 = Pan2.ar(sig2, 0.8);
			sig2 = sig2 * -5.dbamp;

			sig3 = GrainBuf.ar(
				2,
				Impulse.ar([noise1, noise2] ++ SinOsc.kr(0.329, [0,pi], 16)),
				LFNoise0.kr(1, 0.0007, 0.001),
				[buf_l, buf_r],
				2 + SinOsc.kr(0.129, [0,pi]),
				[0.4, 0.6] ++ SinOsc.ar(0.25, mul: 0.02, add: 0.3),
				pan: SinOsc.kr(0.229, [0,pi])
			);

			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			env1 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: titania_pow/130);
			env2 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: oberon_pow/130);
			env3 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: ring_pow/150);
			sig = (sig * env) + (sig1 * env1) + (sig2 * env2) + (sig3 * env3);

			sig = Limiter.ar(sig);
			Out.ar(out, sig);
		}).add;


		//Neptune
		//triton_pow(min:0, max:100);
		//pluto_pow()
		//orcus_pow()
		//nereid_pow()
		//ring_pow()
		SynthDef(\neptune, {
			arg buf_l, buf_r, out, gate=0, amp=0.8, triton_pow=0, pluto_pow=0, orcus_pow=0, nereid_pow=0, ring_pow=0, x_pow=0;
			var sig, sig1, sig2, sig3, sig4, env, env1, env2, env3, env4;

			sig = GrainBuf.ar(
				2,
				Impulse.ar(LFTri.kr(0.044, -0.3, 3.888, 4)),
				LFSaw.ar(LFPulse.kr(Dust.ar(0.07), 0, LFNoise0.ar(0.002, 0.2, 0.4), 0.8) + 1.2, -1, 0.038, 0.102),
				[buf_l, buf_r],
				LFNoise0.ar(0.3, 0.428, 0.8),
				[LFSaw.ar(LFNoise0.ar(0.1, 100, 104), 0, 0.1, -0.3), LFSaw.ar(LFNoise0.ar(0.1, 100, 104), 0, 0.15, -0.75)],
			);
			sig = Pan2.ar(sig, LFNoise1.ar(1, 0.9));
			sig = sig * -3.dbamp;

			sig1 = GrainBuf.ar(
				2,
				Impulse.ar(LFTri.kr(0.044, -0.3, 3.888, 4)),
				[SinOsc.kr(0.044, pi/2, 0.018, 0.02),SinOsc.kr(0.044, -pi/2, 0.018, 0.02)],
				[buf_l, buf_r],
				-1,
				[LFSaw.ar(LFNoise0.ar(0.1, 100, 104), 0, 0.1, -0.3), LFSaw.ar(LFNoise0.ar(0.1, 100, 104), 0, 0.15, -0.75)]
			);
			sig1 = HPF.ar(sig1, 80);

			sig2 = GrainBuf.ar(
				2,
				Impulse.ar(LFTri.ar(0.057, -pi/2, 17, 8).clip(1, 20)),
				LFTri.ar(0.057, -pi/2, 0.17, 0.08).clip(0.01, 0.2),
				buf_l,
				LFTri.ar(0.057, -pi/2, 0.5, 0.9).clip(0.8, 1.3),
				LFSaw.kr(0.005, pi/2, 0.5, 0.5),
				pan: LFTri.kr(0.0057, pi/2, 0.5, 0.5)
			);
			sig2 = sig2 * -3.dbamp;

			sig3 = GrainBuf.ar(
				2,
				Impulse.ar(LFTri.ar(0.057, pi/2, 17, 8).clip(1, 20)),
				LFTri.ar(0.057, pi/2, 0.17, 0.08).clip(0.01, 0.2),
				buf_r,
				LFTri.ar(0.057, pi/2, 0.5, 0.9).clip(0.8, 1.3),
				LFSaw.kr(0.005, -pi/2, 0.5, 0.5),
				pan: LFTri.kr(0.0057, -pi/2, 0.5, 0.5)
			);
			sig3 = sig3 * -3.dbamp;

			sig4 = GrainBuf.ar(
				2,
				Impulse.ar(100),
				0.008,
				[buf_l, buf_r],
				LFSaw.kr(0.0355, 1, -0.7, 0.5),
				LFSaw.kr(0.0355, 1, -0.7, 0.5).clip(0,1)
			);
			sig4 = sig4 * -3.dbamp;
			sig4 = HPF.ar(sig4, 120);

			env = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: amp);
			env1 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: triton_pow/100);
			env2 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: pluto_pow/100);
			env3 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: orcus_pow/100);
			env4 = EnvGen.kr(Env.asr(1, 1, 1), gate: gate, levelScale: nereid_pow/100);
			sig = (sig * env) + (sig1 * env1) + (sig2 * env2) + (sig3 * env3) + (sig4 * env4);

			sig = Limiter.ar(sig);
			Out.ar(out, sig);
		}).add;


		//Planet X
		//planetx_pow(min:0, max:100);
		SynthDef(\planetx, {
			arg buf_l, buf_r, out, gate=0, amp=0.8;
			var sig, env;
			sig = GrainBuf.ar(
				2,
				Dust.ar(9),
				0.09,
				[buf_l, buf_r],
				0.9,
				LFNoise1.ar(9, 0.49, 0.5),
				pan: [-1, 1]
			);
			sig = Splay.ar(sig, 0.6, 0.8);
			sig = LPF.ar(sig, 1089);
			sig = sig + SinOsc.ar(LFNoise1.kr(0.0223, 12, 49) + ((sig+0.1)*29), 0, LFNoise1.kr(0.659, 0.044, 0.05));
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

		planetx = Synth.new(\planetx, [
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

		//mercury
		this.addCommand("mercuryGate", "i", { arg msg;
			mercury.set(\gate, msg[1])
		});

		this.addCommand("mercurySelfAmp", "f", { arg msg;
			mercury.set(\amp, msg[1])
		});

		//venus
		this.addCommand("venusGate", "i", { arg msg;
			venus.set(\gate, msg[1])
		});

		this.addCommand("venusSelfAmp", "f", { arg msg;
			venus.set(\amp, msg[1])
		});

		//earth
		this.addCommand("earthGate", "i", { arg msg;
			earth.set(\gate, msg[1])
		});

		this.addCommand("earthSelfAmp", "f", { arg msg;
			earth.set(\amp, msg[1])
		});

		//mars
		this.addCommand("marsGate", "i", { arg msg;
			mars.set(\gate, msg[1])
		});

		this.addCommand("marsSelfAmp", "f", { arg msg;
			mars.set(\amp, msg[1])
		});


		//jupiter
		this.addCommand("jupiterGate", "i", { arg msg;
			jupiter.set(\gate, msg[1])
		});

		this.addCommand("jupiterSelfAmp", "f", { arg msg;
			jupiter.set(\amp, msg[1])
		});


		//saturn
		this.addCommand("saturnGate", "i", { arg msg;
			saturn.set(\gate, msg[1])
		});

		this.addCommand("saturnSelfAmp", "f", { arg msg;
			saturn.set(\amp, msg[1])
		});


		//uranus
		this.addCommand("uranusGate", "i", { arg msg;
			uranus.set(\gate, msg[1])
		});

		this.addCommand("uranusSelfAmp", "f", { arg msg;
			uranus.set(\amp, msg[1])
		});


		//neptune
		this.addCommand("neptuneGate", "i", { arg msg;
			neptune.set(\gate, msg[1])
		});

		this.addCommand("neptuneSelfAmp", "f", { arg msg;
			neptune.set(\amp, msg[1])
		});


		//planet x
		this.addCommand("planetxGate", "i", { arg msg;
			neptune.set(\gate, msg[1])
		});

		this.addCommand("planetxSelfAmp", "f", { arg msg;
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
		planetx.free;
	}
}