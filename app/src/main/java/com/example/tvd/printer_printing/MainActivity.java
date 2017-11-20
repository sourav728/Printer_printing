package com.example.tvd.printer_printing;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tvd.printer_printing.services.BluetoothService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.lvrenyang.io.Canvas;
import com.lvrenyang.io.Pos;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    BluetoothAdapter deviceadapter;
    ProgressDialog printing;
    Button bt_print_text, bt_print_image, bt_print_report;
    Bitmap barcode, img;
    float yaxis = 0;
    private static OutputStream outputStream;
    Pos mPos = BluetoothService.mPos;
    Canvas mCanvas = BluetoothService.mCanvas;

    ExecutorService es = BluetoothService.es;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceadapter = BluetoothAdapter.getDefaultAdapter();
        deviceadapter.enable();

        bt_print_text = (Button) findViewById(R.id.bt_print_text);
        bt_print_text.setOnClickListener(this);
        bt_print_image = (Button) findViewById(R.id.bt_print_image);
        bt_print_image.setOnClickListener(this);
        bt_print_report = (Button) findViewById(R.id.bt_print_report);
        bt_print_report.setOnClickListener(this);

        startService();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    private void startService() {
        Intent intent = new Intent(MainActivity.this, BluetoothService.class);
        startService(intent);
    }

    private void stopService() {
        Intent intent = new Intent(MainActivity.this, BluetoothService.class);
        stopService(intent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_print_text:
                printing = ProgressDialog.show(MainActivity.this, "Printing Text", "Printing Please wait to Complete");
                es.submit(new TaskPrint(mPos));
                break;
            case R.id.bt_print_image:
                printing = ProgressDialog.show(MainActivity.this, "Printing Image", "Printing Please wait to Complete");
                es.submit(new TaskPrint1(mCanvas));
                //printphoto(R.drawable.motu);
                barcode = getBitmap("1110101030468", 1, 600, 45);
                break;

        }
    }

    /***************CODE FOR PRINTING THROUGH CANVAS***************/

    private class TaskPrint1 implements Runnable {
        com.lvrenyang.io.Canvas canvas = null;

        public TaskPrint1(com.lvrenyang.io.Canvas canvas) {
            this.canvas = canvas;
        }

        @Override
        public void run() {
            final boolean bPrintResult = PrintTicket1(getApplicationContext(), canvas, 576, 1600);
            final boolean bIsOpened = canvas.GetIO().IsOpened();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(), bPrintResult ? getResources().getString(R.string.printsuccess) : getResources().getString(R.string.printfailed), Toast.LENGTH_SHORT).show();
                    if (bIsOpened)
                        printing.dismiss();
                    else
                        printing.dismiss();
                }
            });
        }

        private boolean PrintTicket1(Context ctx, com.lvrenyang.io.Canvas canvas, int nPrintWidth, int nPrintHeight) {
            boolean bPrintResult = false;

            Typeface tfNumber = Typeface.createFromAsset(getAssets(), "fonts/DroidSansMono.ttf");
            canvas.CanvasBegin(nPrintWidth, nPrintHeight);
            canvas.SetPrintDirection(0);

            int small_font_height = 20;
            int normal_font_height = 24;
            int double_font_height = 30;
            int pre_normal_text_length = 21;
            int pre_double_text_length = 16;
            int pre_bill_text_length = 26;
            int pre_bill_return_text_length = 23;
            int amount_length = 12;

            yaxis = 30;
            printtext(canvas, space("", 5) + "" + centeralign("Belgavi", 17), tfNumber, 35);
            yaxis = yaxis + 3;

            printtext(canvas, space("", 9) + "" + centeralign("(540038)", 20), tfNumber, 25);

            printboldtext(canvas, space("RRNO", 14) + ":" + space("CTLG9919", 10), tfNumber, 35);

            printboldtext(canvas, space("Account ID", 14) + ":" + space("4346481000", 10), tfNumber, 35);

            printtext(canvas, space("Mtr. Rdr. Code", 20) + "" + space(":", 1) + "" + space("54003916", 10), tfNumber, 25);

            printtext(canvas, space("Tarrif", 20) + "" + space(":", 1) + "" + space("5LT2A2", 10), tfNumber, 25);

            printtext(canvas, space("Sanct Load", 10) + " " + "" + "HP" + ":" + alignright("0", 3) + " " + "" + "KW:" + alignright("5", 3), tfNumber, 25);

            printtext(canvas, space("Billing Period", 10) + ":" + "06/07/2016" + "-" + "06/08/2016", tfNumber, 25);

            printboldtext(canvas, space("Reading Date", 20) + ":" + "11/18/2017", tfNumber, 25);

            printtext(canvas, space("Bill No", 10) + ":" + "123456789" + "-" + "11/18/2017", tfNumber, 25);

            printtext(canvas, space("Meter No", 20) + ":" + "3456789", tfNumber, 25);

            printboldtext(canvas, space("Pres Rdg", 20) + "" + space(":", 1) + "" + space("6232", 10), tfNumber, 25);

            printboldtext(canvas, space("Prev Rdg", 20) + "" + space(":", 1) + "" + space("6130", 10), tfNumber, 25);

            printtext(canvas, space("Constant", 20) + ":" + " " + "1", tfNumber, 25);

            printtext(canvas, space("Consumption", 20) + ":" + " " + "300", tfNumber, 25);

            printtext(canvas, space("Average", 20) + ":" + " " + "200", tfNumber, 25);

            printtext(canvas, space("Recorded MD", 20) + ":" + " " + "1", tfNumber, 25);

            printtext(canvas, space("Power Factor", 20) + ":" + " " + "0.75", tfNumber, 25);

            printtext(canvas, alignright("1.0", 8) + " " + "*" + alignright("40.0", 8) + alignright("40.0", 16), tfNumber, 25);

            printtext(canvas, alignright("1.0", 8) + " " + "*" + alignright("50.0", 8) + alignright("50.0", 16), tfNumber, 25);

            printtext(canvas,"\n", tfNumber, 10);

            printtext(canvas, alignright("30.0", 8) + " " + "*" + alignright("3.25", 8) + alignright("97.50", 16), tfNumber, 25);

            printtext(canvas, alignright("70.0", 8) + " " + "*" + alignright("4.70.0", 8) + alignright("329.00", 16), tfNumber, 25);

            printtext(canvas, alignright("100.0", 8) + " " + "*" + alignright("6.25.0", 8) + alignright("518.75", 16), tfNumber, 25);

            printtext(canvas, alignright("150.0", 8) + " " + "*" + alignright("7.15.0", 8) + alignright("1072.50", 16), tfNumber, 25);

            printtext(canvas,"\n", tfNumber, 10);

            printtext(canvas, space("Rebates/TOD", 20) + ":" + "" + " " + alignright("0.00", 12), tfNumber, 25);

            printtext(canvas, space("PF Penalty", 20) + ":" + " " + alignright("0.00", 12), tfNumber, 25);

            printtext(canvas, space("MD Penalty", 20) + ":" + " " + alignright("0.00", 12), tfNumber, 25);

            printtext(canvas, space("Interest", 20) + ":" + " " + alignright("1.00", 12), tfNumber, 25);

            printtext(canvas, space("Others", 20) + ":" + " " + alignright("0.00", 12), tfNumber, 25);

            printtext(canvas, space("Tax", 20) + ":" + " " + alignright("60.00", 12), tfNumber, 25);

            printtext(canvas, space("Cur Bill Amt", 20) + ":" + " " + alignright("21.00", 12), tfNumber, 25);

            printtext(canvas, space("Arrears", 20) + ":" + " " + alignright("200.00", 12), tfNumber, 25);

            printtext(canvas, space("Credits & Adj", 20) + ":" + " " + alignright("0.00", 12), tfNumber, 25);

            printtext(canvas, space("GOK Subsidy", 20) + ":" + " " + alignright("0.00", 12), tfNumber, 25);

            printboldtext(canvas, space("Net Amt Due", 13) + ":" + " " + alignright("202.00", 12), tfNumber, 31);

            printtext(canvas, space("Due date", 15) + ":" + " " + alignright("11/18/2017", 12), tfNumber, 25);

            printtext(canvas, space("Printed on", 15) + ":" + " " + alignright(currentDateandTime(), 18), tfNumber, 25);

            canvas.DrawBitmap(barcode, 0, yaxis + 10, 0);


            canvas.CanvasPrint(1, 0);
            bPrintResult = canvas.GetIO().IsOpened();
            return bPrintResult;
        }



        private void printboldtext(com.lvrenyang.io.Canvas canvas, String text, Typeface tfNumber, float textsize) {
            yaxis++;
            canvas.DrawText(text + "\r\n", 0, yaxis, 0, tfNumber, textsize, com.lvrenyang.io.Canvas.FONTSTYLE_BOLD);
            if (textsize == 20) {
                yaxis = yaxis + textsize + 8;
            } else yaxis = yaxis + textsize + 6;
        }


        public String alignright(String msg, int len) {
            for (int i = 0; i < len - msg.length(); i++) {
                msg = " " + msg;
            }
            msg = String.format("%" + len + "s", msg);
            return msg;
        }


        public String centeralign(String text, int width) {
            int count = text.length();
            int value = width - count;
            int append = (value / 2);
            return space(" ", append) + text;
        }

        public String space(String s, int len) {
            int temp;
            StringBuilder spaces = new StringBuilder();
            temp = len - s.length();
            for (int i = 0; i < temp; i++) {
                spaces.append(" ");
            }
            return (s + spaces);
        }

        private void printtext(com.lvrenyang.io.Canvas canvas, String text, Typeface tfNumber, float textsize) {
            yaxis++;
            canvas.DrawText(text + "\r\n", 0, yaxis, 0, tfNumber, textsize, com.lvrenyang.io.Canvas.DIRECTION_LEFT_TO_RIGHT);
            if (textsize == 20) {
                yaxis = yaxis + textsize + 8;
            } else yaxis = yaxis + textsize + 6;
        }
    }





    /*********CODE FOR PRINTER************/
    private class TaskPrint implements Runnable {
        Pos pos = null;

        private TaskPrint(Pos pos) {
            this.pos = pos;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            // final boolean bPrintResult = PrintTicket();//57
            final boolean bPrintResult = PrintTicket();
            final boolean bIsOpened = pos.GetIO().IsOpened();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(), bPrintResult ? getResources().getString(R.string.printsuccess) : getResources().getString(R.string.printfailed), Toast.LENGTH_SHORT).show();
                    if (bIsOpened) {
                        printing.dismiss();
                        // showDialog(EXIT_DLG);
                    } else printing.dismiss();
                }
            });
        }

        /**********DEMO printTicket1()****************/
        public boolean PrintTicket() {
            int total = 0;
            boolean bPrintResult = false;
            int maxlength = 47;
            pos.POS_FeedLine();
            pos.POS_S_Align(1);

            printText(aligncenter("Belgavi", 10));
            printText("(540038)");
            /*******POS_S_Align(1) means center and POS_S_Align(2) means right********/
            pos.POS_S_Align(0);


            //printText(line(maxlength));

            printText(space("RRNO", 22) + ":" + " " + space("CTLG9919", 10));

            printText(space("Account ID", 22) + ":" + " " + space("4346481000", 10));

            printText(space("Mtr.Rdr.Code", 22) + ":" + " " + space("54003916", 10));

            printText(space("Tarrif", 22) + ":" + " " + space("5LT2A2", 10));

            printText(space("Sanct Load", 10) + " " + "HP:" + alignright("0", 5) + " " + "KW:" + alignright("5", 5));

            printText(space("Billing Period", 10) + ":" + " " + "06/07/2016" + "-" + "06/08/2016");

            printText(space("Reading Date", 22) + ":" + " " + "11/18/2017");

            printText(space("Bill No", 10) + ":" + " " + "123456789" + "-" + "11/18/2017");

            printText(space("Meter No", 22) + ":" + " " + "3456789");

            printText(space("Pres Rdg", 22) + ":" + " " + space("6232", 10));

            printText(space("Prev Rdg", 22) + ":" + " " + space("6130", 10));

            printText(space("Constant", 22) + ":" + " " + "1");

            printText(space("Consumption", 22) + ":" + " " + "300");

            printText(space("Average", 22) + ":" + " " + "200");

            printText(space("Recorded MD", 22) + ":" + " " + "1");

            printText(space("Power Factor", 22) + ":" + " " + "0.75");

            printText(alignright("1.0", 8) + " " + "*" + alignright("40.0", 8) + "" + space("", 8) + alignright("40.0", 16));

            printText(alignright("1.0", 8) + " " + "*" + alignright("50.0", 8) + "" + space("", 8) + alignright("50.0", 16));

            printText("\n");

            printText(alignright("30.0", 8) + " " + "*" + alignright("3.25", 8) + "" + space("", 8) + alignright("97.50", 16));

            printText(alignright("70.0", 8) + " " + "*" + alignright("4.70.0", 8) + "" + space("", 8) + alignright("329.00", 16));

            printText(alignright("100.0", 8) + " " + "*" + alignright("6.25.0", 8) + "" + space("", 8) + alignright("518.75", 16));

            printText(alignright("150.0", 8) + " " + "*" + alignright("7.15.0", 8) + "" + space("", 8) + alignright("1072.50", 16));

            printText("\n");

            printText(space("Rebates/TOD", 24) + ":" + " " + alignright("0.00", 16));

            printText(space("PF Penalty", 24) + ":" + " " + alignright("0.00", 16));

            printText(space("MD Penalty", 24) + ":" + " " + alignright("0.00", 16));

            printText(space("Interest", 24) + ":" + " " + alignright("1.00", 16));

            printText(space("Others", 24) + ":" + " " + alignright("0.00", 16));

            printText(space("Tax", 24) + ":" + " " + alignright("60.00", 16));

            printText(space("Cur Bill Amt", 24) + ":" + " " + alignright("21.00", 16));

            printText(space("Arrears", 24) + ":" + " " + alignright("", 16));

            printText(space("Credits & Adj", 24) + ":" + " " + alignright("0.00", 16));

            printText(space("GOK Subsidy", 24) + ":" + " " + alignright("0.00", 16));

            printText(space("Net Amt Due", 24) + ":" + " " + alignright("202.00", 16));

            printText(space("Due date", 22) + ":" + " " + alignright("11/18/2017", 16));

            printText(space("Printed on", 22) + ":" + " " + alignright(currentDateandTime(), 16));


            pos.POS_FeedLine();
            pos.POS_FeedLine();

            bPrintResult = pos.GetIO().IsOpened();
            return bPrintResult;

        }

        private void printText(String msg) {
            pos.POS_S_TextOut(msg + "\r\n", 0, 0, 0, 0, 4);
        }

        private String alignright(String msg, int len) {
            for (int i = 0; i < len - msg.length(); i++) {
                msg = " " + msg;
            }
            msg = String.format("%" + len + "s", msg);
            return msg;
        }

        private String aligncenter(String msg, int len) {
            int count = msg.length();
            int value = len - count;
            int append = (value / 2);
            return space(" ", append) + msg + space(" ", append);
        }

        public String leftalign(String s1, int len) {
            for (int i = 0; i < len - s1.length(); i++) {
                s1 = s1 + " ";
            }
            return (s1);
        }

        private String line(int length) {
            StringBuilder sb5 = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb5.append("-");
            }
            return (sb5.toString());
        }

        public String spaceright(String s, int length) {
            int temp;
            StringBuilder spaces = new StringBuilder();
            temp = length - s.length();
            for (int i = 0; i < temp; i++) {
                spaces.append(" ");
            }
            return (spaces + s);
        }

        private String space(String s, int len) {
            int temp;
            StringBuilder spaces = new StringBuilder();
            temp = len - s.length();
            for (int i = 0; i < temp; i++) {
                spaces.append(" ");
            }
            return (s + spaces);
        }

    }

    private String currentDateandTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String cdt = sdf.format(new Date());
        return cdt;
    }

    /*****************Barcode generation code*******************/

    public Bitmap getBitmap(String barcode, int barcodeType, int width, int height) {
        Bitmap barcodeBitmap = null;
        BarcodeFormat barcodeFormat = convertToZXingFormat(barcodeType);
        try {
            barcodeBitmap = encodeAsBitmap(barcode, barcodeFormat, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return barcodeBitmap;
    }

    private BarcodeFormat convertToZXingFormat(int format) {
        switch (format) {
            case 8:
                return BarcodeFormat.CODABAR;
            case 1:
                return BarcodeFormat.CODE_128;
            case 2:
                return BarcodeFormat.CODE_39;
            case 4:
                return BarcodeFormat.CODE_93;
            case 32:
                return BarcodeFormat.EAN_13;
            case 64:
                return BarcodeFormat.EAN_8;
            case 128:
                return BarcodeFormat.ITF;
            case 512:
                return BarcodeFormat.UPC_A;
            case 1024:
                return BarcodeFormat.UPC_E;
            //default 128?
            default:
                return BarcodeFormat.CODE_128;
        }
    }

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        if (contents == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
            // hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints = new EnumMap(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contents, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

}
