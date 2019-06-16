package com.palopodstreleny.facenet.db.entity

import androidx.room.*
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j


/**
 *
 * Embedding entity represents database table
 *
 * @param userName Foreign key pointing to User table
 * @param 128 embeddings represents point in 128 dimensional space
 *
 */
@Entity(foreignKeys = [ForeignKey(
    entity = User::class,
    parentColumns = arrayOf("userName"),
    childColumns = arrayOf("user_name"))])

data class Embedding(

    @ColumnInfo(name = "user_name")
    val userName: String,


    //128 dimensional embedding

    val e0: Float,
    val e1: Float,
    val e2: Float,
    val e3: Float,
    val e4: Float,
    val e5: Float,
    val e6: Float,
    val e7: Float,
    val e8: Float,
    val e9: Float,

    val e10: Float,
    val e11: Float,
    val e12: Float,
    val e13: Float,
    val e14: Float,
    val e15: Float,
    val e16: Float,
    val e17: Float,
    val e18: Float,
    val e19: Float,

    val e20: Float,
    val e21: Float,
    val e22: Float,
    val e23: Float,
    val e24: Float,
    val e25: Float,
    val e26: Float,
    val e27: Float,
    val e28: Float,
    val e29: Float,

    val e30: Float,
    val e31: Float,
    val e32: Float,
    val e33: Float,
    val e34: Float,
    val e35: Float,
    val e36: Float,
    val e37: Float,
    val e38: Float,
    val e39: Float,

    val e40: Float,
    val e41: Float,
    val e42: Float,
    val e43: Float,
    val e44: Float,
    val e45: Float,
    val e46: Float,
    val e47: Float,
    val e48: Float,
    val e49: Float,

    val e50: Float,
    val e51: Float,
    val e52: Float,
    val e53: Float,
    val e54: Float,
    val e55: Float,
    val e56: Float,
    val e57: Float,
    val e58: Float,
    val e59: Float,

    val e60: Float,
    val e61: Float,
    val e62: Float,
    val e63: Float,
    val e64: Float,
    val e65: Float,
    val e66: Float,
    val e67: Float,
    val e68: Float,
    val e69: Float,

    val e70: Float,
    val e71: Float,
    val e72: Float,
    val e73: Float,
    val e74: Float,
    val e75: Float,
    val e76: Float,
    val e77: Float,
    val e78: Float,
    val e79: Float,

    val e80: Float,
    val e81: Float,
    val e82: Float,
    val e83: Float,
    val e84: Float,
    val e85: Float,
    val e86: Float,
    val e87: Float,
    val e88: Float,
    val e89: Float,

    val e90: Float,
    val e91: Float,
    val e92: Float,
    val e93: Float,
    val e94: Float,
    val e95: Float,
    val e96: Float,
    val e97: Float,
    val e98: Float,
    val e99: Float,

    val e100: Float,
    val e101: Float,
    val e102: Float,
    val e103: Float,
    val e104: Float,
    val e105: Float,
    val e106: Float,
    val e107: Float,
    val e108: Float,
    val e109: Float,

    val e110: Float,
    val e111: Float,
    val e112: Float,
    val e113: Float,
    val e114: Float,
    val e115: Float,
    val e116: Float,
    val e117: Float,
    val e118: Float,
    val e119: Float,

    val e120: Float,
    val e121: Float,
    val e122: Float,
    val e123: Float,
    val e124: Float,
    val e125: Float,
    val e126: Float,
    val e127: Float
)
{

    @Ignore
    val vectorRepresentation: INDArray = Nd4j.create(floatArrayOf(
        e0,
        e1,
        e2,
        e3,
        e4,
        e5,
        e6,
        e7,
        e8,
        e9,

        e10,
        e11,
        e12,
        e13,
        e14,
        e15,
        e16,
        e17,
        e18,
        e19,

        e20,
        e21,
        e22,
        e23,
        e24,
        e25,
        e26,
        e27,
        e28,
        e29,


        e30,
        e31,
        e32,
        e33,
        e34,
        e35,
        e36,
        e37,
        e38,
        e39,


        e40,
        e41,
        e42,
        e43,
        e44,
        e45,
        e46,
        e47,
        e48,
        e49,


        e50,
        e51,
        e52,
        e53,
        e54,
        e55,
        e56,
        e57,
        e58,
        e59,


        e60,
        e61,
        e62,
        e63,
        e64,
        e65,
        e66,
        e67,
        e68,
        e69,


        e70,
        e71,
        e72,
        e73,
        e74,
        e75,
        e76,
        e77,
        e78,
        e79,


        e80,
        e81,
        e82,
        e83,
        e84,
        e85,
        e86,
        e87,
        e88,
        e89,


        e90,
        e91,
        e92,
        e93,
        e94,
        e95,
        e96,
        e97,
        e98,
        e99,


        e100,
        e101,
        e102,
        e103,
        e104,
        e105,
        e106,
        e107,
        e108,
        e109,


        e110,
        e111,
        e112,
        e113,
        e114,
        e115,
        e116,
        e117,
        e118,
        e119,

        e120,
        e121,
        e122,
        e123,
        e124,
        e125,
        e126,
        e127))

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}



