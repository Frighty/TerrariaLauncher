package de.iltisnation.terrarialauncher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class FileWatcher implements Runnable {
	
	private String lockFilePath;
	private LauncherUI ui;
	
	public FileWatcher(LauncherUI ui, String lockFilePath){
		this.ui = ui;
		this.lockFilePath = lockFilePath;
	}
	
	public void run() {
		try {
			final Path path = FileSystems.getDefault().getPath(lockFilePath);
			System.out.println(path);
			final WatchService watchService = FileSystems.getDefault().newWatchService();
			
			final WatchKey watchKey = path.register(watchService, 
						StandardWatchEventKinds.ENTRY_DELETE, 
						StandardWatchEventKinds.ENTRY_CREATE);
			
			while (true) {
			    final WatchKey wk = watchService.take();
			    for (WatchEvent<?> event : wk.pollEvents()) {
			        final Path changed = (Path) event.context();
			        System.out.println(changed);
			        if (changed.toString().equals("lock.ed")) {
			            System.out.println("My file has changed");
			            if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE){
							ui.btnNewButton.setText(ui.STOP_SERVER);
							ui.btnStartClient.setEnabled(true);
			            } 
			            else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE)
			            {
			            	ui.textArea.append("Server stopped.\n");
							ui.btnNewButton.setText(ui.START_SERVER);
							ui.btnStartClient.setEnabled(false);
			            }
			        }
			    }
			    // reset the key
			    boolean valid = wk.reset();
			    if (!valid) {
			        System.out.println("Key has been unregisterede");
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
