import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MusicPlayerApp());
}

class MusicPlayerApp extends StatelessWidget {
  const MusicPlayerApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '音乐播放器',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        useMaterial3: true,
      ),
      home: const MusicPlayerScreen(),
    );
  }
}

class MusicPlayerScreen extends StatefulWidget {
  const MusicPlayerScreen({super.key});

  @override
  State<MusicPlayerScreen> createState() => _MusicPlayerScreenState();
}

class _MusicPlayerScreenState extends State<MusicPlayerScreen> {
  bool isPlaying = false;
  bool isMuted = false;
  double volume = 0.7;

  // 模拟专辑图片
  final String albumArtUrl = 'https://picsum.photos/400/400?music';

  void _togglePlayPause() {
    setState(() {
      isPlaying = !isPlaying;
    });
    // 这里可以添加实际的播放/暂停逻辑
  }

  void _previousTrack() {
    // 上一曲逻辑
  }

  void _nextTrack() {
    // 下一曲逻辑
  }

  void _toggleMute() {
    setState(() {
      isMuted = !isMuted;
    });
    // 静音逻辑
  }

  void _increaseVolume() {
    setState(() {
      if (volume < 1.0) {
        volume += 0.1;
        if (volume > 1.0) volume = 1.0;
      }
    });
    // 增加音量逻辑
  }

  void _decreaseVolume() {
    setState(() {
      if (volume > 0.0) {
        volume -= 0.1;
        if (volume < 0.0) volume = 0.0;
      }
    });
    // 减少音量逻辑
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('音乐播放器'),
        backgroundColor: Colors.deepPurple,
        foregroundColor: Colors.white,
      ),
      body: OrientationBuilder(
        builder: (context, orientation) {
          // 确保始终是竖屏
          if (orientation == Orientation.landscape) {
            // 在横屏时提示用户旋转设备
            return const Center(
              child: Text('请将设备旋转至竖屏模式'),
            );
          }
          
          return Column(
            children: [
              // 专辑封面区域
              Expanded(
                flex: 6,
                child: Container(
                  width: double.infinity,
                  decoration: BoxDecoration(
                    gradient: LinearGradient(
                      begin: Alignment.topCenter,
                      end: Alignment.bottomCenter,
                      colors: [
                        Colors.deepPurple.shade800,
                        Colors.deepPurple.shade400,
                      ],
                    ),
                  ),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      // 专辑图片
                      Container(
                        width: 250,
                        height: 250,
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(20),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withOpacity(0.4),
                              blurRadius: 10,
                              spreadRadius: 2,
                              offset: const Offset(0, 5),
                            ),
                          ],
                        ),
                        child: ClipRRect(
                          borderRadius: BorderRadius.circular(20),
                          child: Image.network(
                            albumArtUrl,
                            fit: BoxFit.cover,
                            loadingBuilder: (context, child, loadingProgress) {
                              if (loadingProgress == null) return child;
                              return Container(
                                color: Colors.grey.shade800,
                                child: Center(
                                  child: CircularProgressIndicator(
                                    value: loadingProgress.expectedTotalBytes !=
                                            null
                                        ? loadingProgress.cumulativeBytesLoaded /
                                            loadingProgress.expectedTotalBytes!
                                        : null,
                                  ),
                                ),
                              );
                            },
                            errorBuilder: (context, error, stackTrace) {
                              return Container(
                                color: Colors.grey.shade800,
                                child: const Icon(
                                  Icons.music_note,
                                  size: 100,
                                  color: Colors.white54,
                                ),
                              );
                            },
                          ),
                        ),
                      ),
                      const SizedBox(height: 30),
                      // 歌曲信息
                      const Text(
                        '歌曲名称',
                        style: TextStyle(
                          fontSize: 24,
                          fontWeight: FontWeight.bold,
                          color: Colors.white,
                        ),
                      ),
                      const SizedBox(height: 8),
                      const Text(
                        '歌手名',
                        style: TextStyle(
                          fontSize: 18,
                          color: Colors.white70,
                        ),
                      ),
                      const SizedBox(height: 20),
                      // 进度条
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 40),
                        child: LinearProgressIndicator(
                          value: 0.4, // 示例进度
                          backgroundColor: Colors.white24,
                          valueColor: AlwaysStoppedAnimation<Color>(
                              Colors.white.withOpacity(0.8)),
                          borderRadius: BorderRadius.circular(10),
                        ),
                      ),
                      const SizedBox(height: 10),
                      const Padding(
                        padding: EdgeInsets.symmetric(horizontal: 40),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              '2:15',
                              style: TextStyle(
                                color: Colors.white70,
                              ),
                            ),
                            Text(
                              '5:30',
                              style: TextStyle(
                                color: Colors.white70,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              // 控制按钮区域
              Expanded(
                flex: 4,
                child: Container(
                  color: Colors.grey.shade100,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      // 播放控制按钮
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          // 上一曲
                          IconButton(
                            onPressed: _previousTrack,
                            icon: const Icon(Icons.skip_previous),
                            iconSize: 40,
                            color: Colors.deepPurple,
                          ),
                          const SizedBox(width: 20),
                          // 播放/暂停
                          Container(
                            decoration: BoxDecoration(
                              shape: BoxShape.circle,
                              color: Colors.deepPurple,
                              boxShadow: [
                                BoxShadow(
                                  color: Colors.deepPurple.withOpacity(0.4),
                                  blurRadius: 10,
                                  spreadRadius: 2,
                                  offset: const Offset(0, 5),
                                ),
                              ],
                            ),
                            child: IconButton(
                              onPressed: _togglePlayPause,
                              icon: Icon(
                                isPlaying ? Icons.pause : Icons.play_arrow,
                                color: Colors.white,
                              ),
                              iconSize: 40,
                            ),
                          ),
                          const SizedBox(width: 20),
                          // 下一曲
                          IconButton(
                            onPressed: _nextTrack,
                            icon: const Icon(Icons.skip_next),
                            iconSize: 40,
                            color: Colors.deepPurple,
                          ),
                        ],
                      ),
                      const SizedBox(height: 30),
                      // 音量控制
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          // 静音
                          IconButton(
                            onPressed: _toggleMute,
                            icon: Icon(
                              isMuted ? Icons.volume_off : Icons.volume_up,
                              color: Colors.deepPurple,
                            ),
                            iconSize: 30,
                          ),
                          const SizedBox(width: 10),
                          // 减小音量
                          IconButton(
                            onPressed: _decreaseVolume,
                            icon: const Icon(Icons.volume_down),
                            iconSize: 30,
                            color: Colors.deepPurple,
                          ),
                          const SizedBox(width: 10),
                          // 音量滑块
                          SizedBox(
                            width: 150,
                            child: Slider(
                              value: volume,
                              onChanged: (value) {
                                setState(() {
                                  volume = value;
                                });
                              },
                              activeColor: Colors.deepPurple,
                              inactiveColor: Colors.deepPurple.shade200,
                            ),
                          ),
                          const SizedBox(width: 10),
                          // 增加音量
                          IconButton(
                            onPressed: _increaseVolume,
                            icon: const Icon(Icons.volume_up),
                            iconSize: 30,
                            color: Colors.deepPurple,
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
            ],
          );
        },
      ),
    );
  }
}