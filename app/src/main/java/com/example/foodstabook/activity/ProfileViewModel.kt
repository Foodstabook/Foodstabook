import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel: ViewModel (){

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()
}

class ProfileState