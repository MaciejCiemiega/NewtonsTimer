import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        DiKt.startDi()
    }

	var body: some Scene {
		WindowGroup {
		    ZStack {
		        Color.white.ignoresSafeArea(.all)
			    ContentView()
			}.preferredColorScheme(.light)
		}
	}
}
