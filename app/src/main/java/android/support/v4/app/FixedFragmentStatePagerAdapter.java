package android.support.v4.app;

import android.os.Bundle;
import android.view.ViewGroup;

public abstract class FixedFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
	public FixedFragmentStatePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment f = (Fragment) super.instantiateItem(container, position);
		Bundle savedFragmentState = f.mSavedFragmentState;
		if (savedFragmentState != null) {
			savedFragmentState.setClassLoader(f.getClass().getClassLoader());
		}
		return f;
	}
}
