package GUI.View.Misc;

import Data.Utils.AnimationUtils;
import com.zero.animation.BaseAnimation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.atomic.AtomicInteger;

public class RingGraph extends JPanel {
    private Ellipse2D.Double innerCircle;
    private Area centerClip;
    private Area ringClip;
    private Line2D.Double[] lines;

    private double[] values;
    private double[] degrees;
    private Color[] colors;
    private Image center;
    private Image resized = null;
    private int ringSize;

    private boolean needsComputing = true;
    private boolean needsAnimation = true;

    public RingGraph(double[] values, Image center, int ringSize) {
        setPreferredSize(new Dimension(300, 300));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                needsComputing = true;
                repaint();
            }
        });

        this.values = values;
        this.center = center;
        this.ringSize = ringSize;

        compute();
    }

    private volatile boolean running;

    public void startAnimation() {
        if (!running) {
            arc.set(0);
            BaseAnimation[] animations = new BaseAnimation[values.length];

            for (int i = 0; i < animations.length; i++)
                animations[i] = new Animator();

            AnimationUtils.getController().addSequence(pAnimation -> running = false, animations);
            running = true;
            needsAnimation = false;
        }
    }

    private void compute() {
        if (needsComputing) {
            int size = Math.min(getWidth(), getHeight());

            if (size == 0)
                size = Math.min(getPreferredSize().width, getPreferredSize().height);

            int centerPoint = size / 2;
            int centerSize = size - ringSize * 2;

            innerCircle = new Ellipse2D.Double(
                    centerPoint - centerSize / 2f + 1,
                    centerPoint - centerSize / 2f + 1,
                    centerSize - 2,
                    centerSize - 2
            );
            centerClip = new Area(innerCircle);

            if (center != null) {
                resized = center.getScaledInstance((int) Math.round(innerCircle.width),
                        (int) Math.round(innerCircle.height), Image.SCALE_SMOOTH);
            }

            double total = 0;

            for (double v : values)
                total += v;

            degrees = new double[values.length];
            colors = new Color[values.length];

            for (int i = 0; i < values.length; i++) {
                degrees[i] = -degreeRatio(values[i], total);
                colors[i] = Color.getHSBColor((i + 1) * 0.3f, 1f, 1f);
            }

            lines = new Line2D.Double[values.length];
            if (lines.length > 0)
                lines[0] = new Line2D.Double(centerPoint, centerPoint, centerPoint, 0);

            double angle = 0;

            for (int i = 0; i < values.length - 1; i++) {
                angle += degrees[i];

                lines[i + 1] = createLine(centerPoint, angle, size);
            }

            Area outerCircle = new Area(new Rectangle2D.Double(0, 0, size, size));
            outerCircle.subtract(centerClip);

            ringClip = outerCircle;
            needsComputing = false;
        }
    }

    private volatile float progress;
    private final AtomicInteger arc = new AtomicInteger(0);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final int degreeOffset = 90;

        compute();

        if (needsAnimation) {
            startAnimation();
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight());

        g2d.setClip(ringClip);

        double angle = 0;
        Color arcColor = colors[0];

        for (int i = 0; i < arc.get() && i < values.length; i++) {
            g2d.setColor(arcColor);

            g2d.fillArc(0, 0, size, size, (int) Math.round(degreeOffset + angle),
                    (int) Math.round(degrees[i]));

            angle += degrees[i];

            if (i < colors.length - 1)
                arcColor = colors[i + 1];
        }

        if (arc.get() < values.length) {
            g2d.setColor(arcColor);
            g2d.fillArc(0, 0, size, size, (int) Math.round(degreeOffset + angle),
                    (int) Math.round(degrees[arc.get()] * progress));
        }

        g2d.setColor(getBackground());
        g2d.setStroke(new BasicStroke(3));

        for (Line2D.Double l: lines)
            g2d.draw(l);

        int x = (int) Math.round(innerCircle.x), y = (int) Math.round(innerCircle.y),
                width = (int) Math.round(innerCircle.width), height = (int) Math.round(innerCircle.height);

        if (center != null) {
            g2d.setClip(centerClip);
            g2d.drawImage(resized, x, y, width, height, null);
        }

        Color base = getBackground();
        Color baseTransparent = new Color(base.getRed(), base.getGreen(), base.getBlue(), 0);

        int offset = 7;
        g2d.setClip(null);
        g2d.setPaint(new RadialGradientPaint(width/2f + x, height/2f + y, width/2f + offset,
                new float[]{0.9f, 0.93f, 0.95f + sizeFractionOffset(size), 1f},
                new Color[] {baseTransparent, base, base, baseTransparent}));
        g2d.setStroke(new BasicStroke(20));
        g2d.drawOval(x - offset/2, y - offset/2, width + offset, height + offset);
    }

    private class Animator extends BaseAnimation {
        public Animator() {
            super(3000 / values.length);
        }

        @Override
        protected void onProgress(float pProgress) {
            progress = pProgress;
            repaint();
        }

        @Override
        protected void onFinish() {
            progress = 0;
            arc.incrementAndGet();
        }
    }

    private double degreeRatio(double value, double total) {
        return (value*360)/total;
    }

    private Line2D.Double createLine(int center, double angle, int size) {
        int x2, y2;
        angle = -angle;

        if(angle < 45) {
            y2 = 0;
            x2 = center + (int) Math.round(Math.tan(Math.toRadians(angle)) * center);
        } else if (angle < 90) {
            x2 = size;
            y2 = center - (int) Math.round(Math.tan(Math.toRadians(90 - angle)) * center);
        } else  if (angle < 135) {
            x2 = size;
            y2 = center + (int) Math.round(Math.tan(Math.toRadians(angle - 90)) * center);
        } else if (angle < 180) {
            y2 = size;
            x2 = center + (int) Math.round(Math.tan(Math.toRadians(180 - angle)) * center);
        } else if (angle < 225) {
            y2 = size;
            x2 = center - (int) Math.round(Math.tan(Math.toRadians(angle - 180)) * center);
        } else if (angle < 270) {
            x2 = 0;
            y2 = center + (int) Math.round(Math.tan(Math.toRadians(270 - angle)) * center);
        } else if (angle < 315) {
            x2 = 0;
            y2 = center - (int) Math.round(Math.tan(Math.toRadians(angle - 270)) * center);
        } else {
            y2 = 0;
            x2 = center - (int) Math.round(Math.tan(Math.toRadians(360 - angle)) * center);
        }

        return new Line2D.Double(center, center, x2, y2);
    }

    private float sizeFractionOffset(int size) {
        float result = (float) (Math.log10(size) - 2.6)/10;

        if (result < -0.01f)
            return -0.01f;
        else if (result > 0.045f)
            return 0.045f;

        return result;
    }

    public Color[] getColors() {
        return colors;
    }

    public void setValues(double[] values) {
        this.values = values;
        needsComputing = true;
        repaint();
    }

    public void setCenter(Image center) {
        this.center = center;
        repaint();
    }

    public void setRingSize(int ringSize) {
        this.ringSize = ringSize;
        needsComputing = true;
        repaint();
    }
}
