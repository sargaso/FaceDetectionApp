package com.example.facedetectionapp.face_detection

import android.graphics.*
import androidx.annotation.ColorInt
import com.example.facedetectionapp.camera.GraphicOverlay
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour


class FaceContourGraphic(
    overlay: GraphicOverlay,
    private val face: Face,
    private val imageRect: Rect
) : GraphicOverlay.Graphic(overlay) {

    private val facePositionPaint: Paint
    private val idPaint: Paint
    private val boxPaint: Paint

    init {
        val selectedColor = Color.WHITE

        facePositionPaint = Paint()
        facePositionPaint.color = selectedColor

        idPaint = Paint()
        idPaint.color = selectedColor
        idPaint.textSize = ID_TEXT_SIZE

        boxPaint = Paint()
        boxPaint.color = selectedColor
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = BOX_STROKE_WIDTH
    }

    private fun Canvas.drawFace(facePosition: Int, @ColorInt selectedColor: Int) {
        val contour = face.getContour(facePosition)
        val path = Path()
        contour?.points?.forEachIndexed { index, pointF ->
            if (index == 0) {
                path.moveTo(
                    translateX(pointF.x),
                    translateY(pointF.y)
                )
            }
            path.lineTo(
                translateX(pointF.x),
                translateY(pointF.y)
            )
        }
        val paint = Paint().apply {
            color = selectedColor
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        }
        drawPath(path, paint)
    }

    override fun draw(canvas: Canvas?) {

        val rect = calculateRect(
            imageRect.height().toFloat(),
            imageRect.width().toFloat(),
            face.boundingBox
        )
        canvas?.drawRect(rect, boxPaint)

        val contours = face.allContours

        contours.forEach {
            it.points.forEach { point ->
                val px = translateX(point.x)
                val py = translateY(point.y)
                canvas?.drawCircle(px, py, FACE_POSITION_RADIUS, facePositionPaint)
            }
        }


        canvas?.drawFace(FaceContour.FACE, Color.BLUE)


        canvas?.drawFace(FaceContour.LEFT_EYEBROW_TOP, Color.RED)
        canvas?.drawFace(FaceContour.LEFT_EYEBROW_BOTTOM, Color.MAGENTA)
        canvas?.drawFace(FaceContour.RIGHT_EYEBROW_TOP, Color.RED)
        canvas?.drawFace(FaceContour.RIGHT_EYEBROW_BOTTOM, Color.MAGENTA)

        canvas?.drawFace(FaceContour.LEFT_EYE, Color.YELLOW)
        canvas?.drawFace(FaceContour.RIGHT_EYE, Color.YELLOW)

        canvas?.drawFace(FaceContour.NOSE_BOTTOM, Color.MAGENTA)
        canvas?.drawFace(FaceContour.NOSE_BRIDGE, Color.MAGENTA)



        canvas?.drawFace(FaceContour.UPPER_LIP_TOP, Color.RED)
        canvas?.drawFace(FaceContour.UPPER_LIP_BOTTOM, Color.MAGENTA)
        canvas?.drawFace(FaceContour.LOWER_LIP_TOP, Color.RED)
        canvas?.drawFace(FaceContour.LOWER_LIP_BOTTOM, Color.MAGENTA)

    }

    companion object {
        private const val FACE_POSITION_RADIUS = 4.0f
        private const val ID_TEXT_SIZE = 30.0f
        private const val BOX_STROKE_WIDTH = 5.0f
    }

}