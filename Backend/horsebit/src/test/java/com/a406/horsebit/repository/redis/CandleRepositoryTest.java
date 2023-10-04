package com.a406.horsebit.repository.redis;

import com.a406.horsebit.constant.CandleConstant;
import com.a406.horsebit.domain.redis.Candle;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CandleRepositoryTest {
    private final RedissonClient redissonClient;
    private final CandleRepository candleRepository;

    private static final String CANDLE_PREFIX = "CANDLE_";
    private static final String CANDLE_INITIAL_TIME_PREFIX = "CANDLE_INITIAL_TIME:";

    @Autowired
    public CandleRepositoryTest(RedissonClient redissonClient, CandleRepository candleRepository) {
        this.redissonClient = redissonClient;
        this.candleRepository = candleRepository;
    }

    private String listNameGenerator(Long tokenNo, String candleType) {
        return CANDLE_PREFIX + candleType + ":" + tokenNo;
    }

    private int indexFinder(int listSize, int index) {
        return Math.min(listSize, index);
    }

    @Test
    void saveCandleData() {
        for(long tokenNo = 1; tokenNo <= 25; ++tokenNo) {
            LocalDateTime endTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            long dataSize = 180;
            RBucket<LocalDateTime> initialTimeRBucket = redissonClient.getBucket(CANDLE_INITIAL_TIME_PREFIX + tokenNo);
            initialTimeRBucket.set(endTime.minusMinutes(dataSize).minusHours(9L));
            List<Candle> candleList = Arrays.asList(
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3072L, 3101L, 3138L, 3046L, 113.298),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3101L, 3065L, 3139L, 3063L, 7805.761),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3065L, 3084L, 3141L, 3013L, 5806.111),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3084L, 3050L, 3154L, 3002L, 6899.26),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3050L, 3036L, 3135L, 2954L, 4222.673),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3036L, 3080L, 3090L, 3024L, 186.03),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3080L, 3063L, 3104L, 3025L, 1285.372),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3063L, 3111L, 3195L, 3000L, 4923.617),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3111L, 3063L, 3155L, 2992L, 4329.464),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3063L, 3041L, 3098L, 2948L, 7786.691),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3041L, 3063L, 3113L, 3024L, 3312.589),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3063L, 3069L, 3082L, 3043L, 6562.111),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3069L, 3026L, 3087L, 3014L, 1119.367),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3026L, 2999L, 3067L, 2921L, 8502.167),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2999L, 2954L, 3082L, 2932L, 9659.949),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2954L, 2979L, 3065L, 2939L, 2591.662),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2979L, 3018L, 3083L, 2901L, 1666.574),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3018L, 3040L, 3136L, 2987L, 6629.937),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3040L, 2997L, 3094L, 2943L, 7327.681),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2997L, 3037L, 3100L, 2986L, 2030.987),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3037L, 3047L, 3075L, 2958L, 865.266),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3047L, 3077L, 3164L, 3000L, 9851.376),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3077L, 3109L, 3114L, 2992L, 9161.471),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3109L, 3111L, 3154L, 3029L, 1686.322),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3111L, 3087L, 3181L, 2998L, 7648.959),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3087L, 3123L, 3181L, 3031L, 290.189),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3123L, 3094L, 3203L, 3039L, 714.409),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3094L, 3083L, 3160L, 3042L, 7520.371),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3083L, 3133L, 3210L, 2989L, 5103.348),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3133L, 3163L, 3234L, 3116L, 8941.733),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3163L, 3145L, 3254L, 3058L, 9924.235),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3145L, 3164L, 3256L, 3097L, 9810.907),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3164L, 3147L, 3171L, 3133L, 4053.551),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3147L, 3165L, 3203L, 3062L, 9474.957),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3165L, 3190L, 3258L, 3091L, 3389.957),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3190L, 3171L, 3286L, 3074L, 1519.882),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3171L, 3201L, 3264L, 3135L, 5923.461),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3201L, 3236L, 3323L, 3110L, 5094.927),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3236L, 3240L, 3300L, 3200L, 1179.103),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3240L, 3201L, 3243L, 3184L, 3300.323),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3201L, 3161L, 3203L, 3156L, 3044.39),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3161L, 3133L, 3172L, 3067L, 5457.034),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3133L, 3085L, 3154L, 3075L, 3068.907),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3085L, 3120L, 3209L, 3021L, 8824.139),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3120L, 3165L, 3174L, 3023L, 8193.708),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3165L, 3178L, 3268L, 3111L, 563.91),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3178L, 3208L, 3226L, 3136L, 2829.938),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3208L, 3168L, 3268L, 3119L, 737.367),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3168L, 3172L, 3233L, 3096L, 8797.877),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3172L, 3120L, 3242L, 3055L, 8119.207),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3120L, 3130L, 3173L, 3037L, 9247.207),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3130L, 3129L, 3195L, 3098L, 7684.023),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3129L, 3070L, 3151L, 3068L, 9917.154),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3070L, 3095L, 3169L, 3054L, 3326.76),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3095L, 3083L, 3192L, 3043L, 5103.701),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3083L, 3085L, 3091L, 3029L, 4427.992),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3085L, 3082L, 3116L, 3034L, 6861.662),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3082L, 3055L, 3157L, 2987L, 1318.069),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3055L, 3027L, 3071L, 2984L, 3603.695),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3027L, 2997L, 3069L, 2898L, 7774.355),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2997L, 2936L, 3090L, 2853L, 1680.672),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2936L, 2957L, 2978L, 2864L, 641.868),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2957L, 2973L, 2988L, 2899L, 9880.456),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2973L, 2987L, 3050L, 2892L, 7540.842),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2987L, 2983L, 3051L, 2982L, 9061.913),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2983L, 2961L, 3033L, 2937L, 1643.992),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2961L, 2959L, 2998L, 2901L, 6651.326),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2959L, 2972L, 3020L, 2909L, 6797.541),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2972L, 2941L, 3019L, 2890L, 4114.688),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2941L, 2966L, 3017L, 2843L, 9088.524),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2966L, 2982L, 2998L, 2868L, 1259.032),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2982L, 2994L, 3041L, 2897L, 1002.174),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2994L, 3004L, 3093L, 2926L, 7567.733),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3004L, 3033L, 3109L, 2935L, 7751.62),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3033L, 3024L, 3069L, 3018L, 3601.744),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3024L, 3031L, 3059L, 2960L, 1896.291),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3031L, 3047L, 3128L, 2962L, 5440.214),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3047L, 3001L, 3112L, 2995L, 8522.865),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3001L, 3047L, 3127L, 2904L, 6709.956),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3047L, 3035L, 3068L, 3002L, 4484.015),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3035L, 2993L, 3118L, 2982L, 721.845),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2993L, 3015L, 3047L, 2950L, 7483.501),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3015L, 2989L, 3085L, 2964L, 7191.986),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2989L, 2942L, 2997L, 2890L, 78.258),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2942L, 2903L, 3009L, 2818L, 3454.105),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2903L, 2922L, 2922L, 2889L, 6004.332),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2922L, 2882L, 2985L, 2794L, 1473.254),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2882L, 2834L, 2883L, 2790L, 7370.205),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2834L, 2819L, 2927L, 2758L, 2838.8),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2819L, 2753L, 2897L, 2739L, 1539.631),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2753L, 2720L, 2756L, 2622L, 2486.476),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2720L, 2692L, 2769L, 2647L, 8816.634),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2692L, 2677L, 2772L, 2597L, 4346.506),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2677L, 2672L, 2752L, 2644L, 4228.796),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2672L, 2707L, 2724L, 2611L, 380.225),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2707L, 2735L, 2830L, 2656L, 268.798),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2735L, 2733L, 2744L, 2635L, 3759.562),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2733L, 2746L, 2774L, 2699L, 3981.864),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2746L, 2777L, 2809L, 2706L, 7178.675),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2777L, 2785L, 2850L, 2695L, 4196.543),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2785L, 2828L, 2894L, 2759L, 2598.896),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2828L, 2893L, 2925L, 2812L, 8294.296),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2893L, 2878L, 2899L, 2852L, 2631.507),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2878L, 2913L, 2942L, 2855L, 8888.97),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2913L, 2990L, 2995L, 2834L, 842.167),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2990L, 2995L, 2995L, 2922L, 9653.365),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2995L, 3018L, 3062L, 2972L, 2385.234),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3018L, 3040L, 3054L, 2939L, 1676.3),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3040L, 3024L, 3091L, 2991L, 9803.748),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3024L, 3054L, 3150L, 2933L, 6467.889),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3054L, 3033L, 3129L, 3000L, 9332.677),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3033L, 3047L, 3100L, 2973L, 8700.192),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3047L, 3071L, 3115L, 3015L, 5913.206),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3071L, 3052L, 3131L, 3008L, 6383.348),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3052L, 3086L, 3177L, 2974L, 7967.295),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3086L, 3079L, 3092L, 2991L, 5995.283),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3079L, 3069L, 3166L, 3019L, 5773.278),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3069L, 3038L, 3072L, 2999L, 3135.976),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3038L, 3086L, 3141L, 2945L, 4257.768),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3086L, 3099L, 3106L, 3021L, 4399.544),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3099L, 3142L, 3223L, 3059L, 6872.208),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3142L, 3171L, 3243L, 3078L, 9326.996),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3171L, 3191L, 3269L, 3096L, 1059.19),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3191L, 3205L, 3240L, 3189L, 2342.725),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3205L, 3217L, 3295L, 3203L, 276.755),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3217L, 3257L, 3291L, 3133L, 5615.976),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3257L, 3254L, 3313L, 3211L, 4185.721),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3254L, 3225L, 3311L, 3185L, 7228.967),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3225L, 3244L, 3318L, 3140L, 156.582),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3244L, 3254L, 3305L, 3237L, 9767.252),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3254L, 3312L, 3386L, 3170L, 1977.601),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3312L, 3310L, 3347L, 3268L, 2245.328),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3310L, 3328L, 3347L, 3237L, 5895.739),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3328L, 3357L, 3369L, 3257L, 8768.761),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3357L, 3350L, 3420L, 3260L, 1629.241),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3350L, 3409L, 3507L, 3267L, 7074.853),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3409L, 3380L, 3482L, 3356L, 101.951),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3380L, 3419L, 3428L, 3305L, 2851.126),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3419L, 3456L, 3551L, 3397L, 7878.822),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3456L, 3475L, 3529L, 3440L, 2033.801),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3475L, 3494L, 3571L, 3443L, 7359.525),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3494L, 3474L, 3517L, 3398L, 2414.591),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3474L, 3524L, 3533L, 3414L, 8275.431),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3524L, 3556L, 3569L, 3514L, 8805.824),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3556L, 3583L, 3619L, 3508L, 8408.732),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3583L, 3542L, 3677L, 3511L, 284.645),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3542L, 3492L, 3638L, 3426L, 9970.131),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3492L, 3476L, 3561L, 3457L, 3327.144),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3476L, 3419L, 3501L, 3351L, 4121.069),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3419L, 3437L, 3530L, 3326L, 6052.735),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3437L, 3451L, 3510L, 3362L, 7487.143),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3451L, 3461L, 3471L, 3407L, 8437.77),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3461L, 3403L, 3463L, 3395L, 4663.891),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3403L, 3359L, 3412L, 3346L, 438.795),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3359L, 3327L, 3381L, 3233L, 3320.757),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3327L, 3292L, 3329L, 3259L, 8510.173),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3292L, 3257L, 3326L, 3229L, 3502.636),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3257L, 3214L, 3325L, 3171L, 1328.326),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3214L, 3180L, 3262L, 3140L, 9725.026),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3180L, 3214L, 3291L, 3150L, 3894.712),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3214L, 3200L, 3237L, 3137L, 8482.725),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3200L, 3171L, 3298L, 3084L, 8028.712),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3171L, 3188L, 3285L, 3075L, 6843.97),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3188L, 3137L, 3193L, 3061L, 5843.355),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3137L, 3120L, 3175L, 3117L, 4069.514),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3120L, 3081L, 3161L, 3027L, 5969.486),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3081L, 3104L, 3147L, 3063L, 5224.025),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3104L, 3101L, 3184L, 3073L, 8957.728),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3101L, 3064L, 3107L, 3061L, 4912.311),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3064L, 3015L, 3069L, 2948L, 796.605),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3015L, 2988L, 3101L, 2906L, 6062.385),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 2988L, 3037L, 3088L, 2973L, 4559.159),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3037L, 3032L, 3131L, 2947L, 7740.377),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3032L, 3067L, 3123L, 2953L, 5198.094),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3067L, 3061L, 3139L, 3053L, 3107.202),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3061L, 3054L, 3155L, 3015L, 8405.761),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3054L, 3024L, 3111L, 2936L, 8458.726),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3024L, 3052L, 3070L, 3010L, 8306.845),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3052L, 3019L, 3057L, 2981L, 7042.352),
                    new Candle(LocalDateTime.now(ZoneId.of("Asia/Seoul")), 3019L, 3000L, 3117L, 2985L, 2378.965)
            );
            RList<Candle> candleRList = redissonClient.getList(listNameGenerator(tokenNo, CandleConstant.CANDLE_TYPE_LIST.get(0).getCandleType()));
            candleRList.delete();
            for(long index = dataSize; 0 < index; --index) {
                Candle candle = candleList.get((int) (dataSize - index));
                candle.setStartTime(endTime.minusMinutes(index).minusHours(9L)); // local: seoul, server: gmt
                candleRList.add(candle);
            }
        }
    }

    @Test
    void findOneByTokenNo() {
    }

    @Test
    void findRangeByTokenNo() {
    }

    @Test
    void findCandleInitialTime() {
    }
}