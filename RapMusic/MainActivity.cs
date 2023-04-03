using System;
using Android.App;
using Android.OS;
using Android.Runtime;
using AndroidX.AppCompat.App;
using Android.Util;
using Android.Widget;
using System.IO;
using System.Net;

namespace RapMusic
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme.NoActionBar", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {
        public TextView TextDisplay { get; private set; }
        public EditText addrText { get; private set; }

        private string[] path = {
            "/play/pause",
            "/play/next",
            "/play/previous",
            "/play/volup",
            "/play/voldown",
            "/play/like",
            "/play/lyric",
            "/play/mute",
            "/sys/getvol"
        };

        private static string httpPlayControl(string addr, string path)
        {
            if (addr == "" || path == "") return "";
            string SingJumpUrl = "http://" + addr + ":" + "18890" + path;
            string respStr = "";
            Log.Debug("url", SingJumpUrl);
            try
            {
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(SingJumpUrl);
                request.Method = "GET";
                request.ContentType = "application/json";
                request.UserAgent = null;

                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                Stream respStream = response.GetResponseStream();
                StreamReader sr = new StreamReader(respStream);
                respStr = sr.ReadToEnd();
                sr.Close();
                respStream.Close();
            }
            catch (Exception ex)
            {
                Toast.MakeText(Application.Context, ex.ToString(), ToastLength.Short).Show();
            }

            return respStr;
        }

        private void OnTextEditChange()
        {
            string SingJumpAddr = addrText.Text;
            string saveFileName = Path.Combine(System.Environment.GetFolderPath(System.Environment.SpecialFolder.LocalApplicationData), "SingJumpAddr");
            File.WriteAllText(saveFileName, SingJumpAddr);
        }

        private void loadAddrSetting()
        {
            string path = Path.Combine(System.Environment.GetFolderPath(System.Environment.SpecialFolder.LocalApplicationData), "SingJumpAddr");
            string addr = File.ReadAllText(path);
            addrText.Text = addr;
        }

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_main);

            addrText = FindViewById<EditText>(Resource.Id.addr_text);

            Button pauseButton = FindViewById<Button>(Resource.Id.pause_button);
            pauseButton.Click += OnPlayButton_Click;

            TextDisplay = FindViewById<TextView>(Resource.Id.screen_display);

            Button upButton = FindViewById<Button>(Resource.Id.up_button);
            upButton.Click += OnUpButton_Click;

            Button downButton = FindViewById<Button>(Resource.Id.down_button);
            downButton.Click += OnDownButton_Click;

            Button nextButton = FindViewById<Button>(Resource.Id.next_button);
            nextButton.Click += OnNextButton_Click;

            Button prevButton = FindViewById<Button>(Resource.Id.prev_button);
            prevButton.Click += OnPrevButton_Click;

            Button lyricButton = FindViewById<Button>(Resource.Id.lyric_button);
            lyricButton.Click += OnLyricButton_Click;

            Button muteButton = FindViewById<Button>(Resource.Id.mute_button);
            muteButton.Click += OnMuteButton_Click;

            Button likeButton = FindViewById<Button>(Resource.Id.like_button);
            likeButton.Click += OnLikeButton_Click;
        }

        public void OnUpButton_Click(object sender, System.EventArgs e)
        {
            string resp = httpPlayControl(addrText.Text, path[3]);
            Log.Debug("btn", addrText.Text, resp);
        }

        public void OnDownButton_Click(object sender, System.EventArgs e)
        {
            string resp = httpPlayControl(addrText.Text, path[4]);
            Log.Debug("btn", addrText.Text, resp);
        }

        public void OnPlayButton_Click(Object sender, System.EventArgs e)
        {
            string resp = httpPlayControl(addrText.Text, path[0]);
            Log.Debug("btn", addrText.Text, resp);

            RunOnUiThread(() => { TextDisplay.Text = "Rap Music!"; });
            OnTextEditChange();
        }

        public void OnNextButton_Click(Object sender, System.EventArgs e)
        {
            string resp = httpPlayControl(addrText.Text, path[1]);
            Log.Debug("btn", addrText.Text, resp);
        }

        public void OnPrevButton_Click(Object sender, System.EventArgs e)
        {
            string resp = httpPlayControl(addrText.Text, path[2]);
            Log.Debug("btn", addrText.Text, resp);
        }

        public void OnLikeButton_Click(Object sender, System.EventArgs e)
        {
            string resp = httpPlayControl(addrText.Text, path[5]);
            Log.Debug("btn", addrText.Text, resp);
        }

        public void OnLyricButton_Click(Object sender, System.EventArgs e)
        {
            string resp = httpPlayControl(addrText.Text, path[6]);
            Log.Debug("btn", addrText.Text, resp);
        }

        public void OnMuteButton_Click(Object sender, System.EventArgs e)
        {
            string resp = httpPlayControl(addrText.Text, path[7]);
            Log.Debug("btn", addrText.Text, resp);
        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
	}
}
